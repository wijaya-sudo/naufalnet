package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ExamDataProvider
import com.example.data.local.ExamDatabase
import com.example.data.local.ExamResultEntity
import com.example.model.ExamPackage
import com.example.model.FontSizeMode
import com.example.model.Screen
import com.example.model.StudentProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONObject

class CbtViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ExamDatabase.getDatabase(application)
    private val resultDao = db.examResultDao()

    // Screen State Navigation
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Student Profile
    private val _studentProfile = MutableStateFlow(StudentProfile())
    val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

    // Available Packages
    private val _packages = MutableStateFlow<List<ExamPackage>>(ExamDataProvider.getAllPackages())
    val packages: StateFlow<List<ExamPackage>> = _packages.asStateFlow()

    // Current Selected Package
    private val _selectedPackage = MutableStateFlow<ExamPackage?>(null)
    val selectedPackage: StateFlow<ExamPackage?> = _selectedPackage.asStateFlow()

    // Token Input & Validation
    private val _tokenInput = MutableStateFlow("")
    val tokenInput: StateFlow<String> = _tokenInput.asStateFlow()

    private val _tokenError = MutableStateFlow<String?>(null)
    val tokenError: StateFlow<String?> = _tokenError.asStateFlow()

    // Exam Session State
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    // Map: Question Index -> Selected Option ID ("A", "B", etc.)
    private val _userAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val userAnswers: StateFlow<Map<Int, String>> = _userAnswers.asStateFlow()

    // Set of Question Indices marked as Doubtful ("Ragu-Ragu")
    private val _doubtfulQuestions = MutableStateFlow<Set<Int>>(emptySet())
    val doubtfulQuestions: StateFlow<Set<Int>> = _doubtfulQuestions.asStateFlow()

    // Timer (in seconds)
    private val _remainingTimeSeconds = MutableStateFlow(0L)
    val remainingTimeSeconds: StateFlow<Long> = _remainingTimeSeconds.asStateFlow()
    private var timerJob: Job? = null

    private var examStartTimeMillis = 0L

    // Anti-Cheat Violation Monitoring
    private val _violationsCount = MutableStateFlow(0)
    val violationsCount: StateFlow<Int> = _violationsCount.asStateFlow()

    private val _showViolationDialog = MutableStateFlow(false)
    val showViolationDialog: StateFlow<Boolean> = _showViolationDialog.asStateFlow()

    // Accessibility Font Size Mode
    private val _fontSizeMode = MutableStateFlow(FontSizeMode.NORMAL)
    val fontSizeMode: StateFlow<FontSizeMode> = _fontSizeMode.asStateFlow()

    // UI Dialogs & Bottom Sheet
    private val _showQuestionNavigator = MutableStateFlow(false)
    val showQuestionNavigator: StateFlow<Boolean> = _showQuestionNavigator.asStateFlow()

    private val _showSubmitConfirmDialog = MutableStateFlow(false)
    val showSubmitConfirmDialog: StateFlow<Boolean> = _showSubmitConfirmDialog.asStateFlow()

    // Results from Database
    private val _allResults = MutableStateFlow<List<ExamResultEntity>>(emptyList())
    val allResults: StateFlow<List<ExamResultEntity>> = _allResults.asStateFlow()

    private val _latestResult = MutableStateFlow<ExamResultEntity?>(null)
    val latestResult: StateFlow<ExamResultEntity?> = _latestResult.asStateFlow()

    init {
        viewModelScope.launch {
            resultDao.getAllResults().collectLatest { list ->
                _allResults.value = list
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun updateStudentProfile(fullName: String, idNumber: String, school: String, room: String) {
        _studentProfile.value = _studentProfile.value.copy(
            fullName = fullName.ifBlank { "Peserta Ujian" },
            idNumber = idNumber.ifBlank { "2026-CBT-0001" },
            schoolOrInstitution = school.ifBlank { "SMA / Instansi Peserta" },
            examRoom = room.ifBlank { "Ruang CBT 01" }
        )
    }

    fun selectPackage(pkgId: String) {
        val pkg = ExamDataProvider.getPackageById(pkgId)
        _selectedPackage.value = pkg
        _tokenInput.value = ""
        _tokenError.value = null
        if (pkg != null) {
            _currentScreen.value = Screen.Instruction(pkgId)
        }
    }

    fun onTokenInputChange(input: String) {
        _tokenInput.value = input.uppercase().trim()
        _tokenError.value = null
    }

    fun fillSampleToken() {
        _selectedPackage.value?.let { pkg ->
            _tokenInput.value = pkg.token
            _tokenError.value = null
        }
    }

    fun verifyTokenAndStartExam() {
        val pkg = _selectedPackage.value ?: return
        val entered = _tokenInput.value.trim()
        if (entered.equals(pkg.token, ignoreCase = true)) {
            _tokenError.value = null
            startExam(pkg)
        } else {
            _tokenError.value = "Token ujian salah atau tidak valid! Silakan periksa kembali token resmi."
        }
    }

    private fun startExam(pkg: ExamPackage) {
        timerJob?.cancel()
        _currentQuestionIndex.value = 0
        _userAnswers.value = emptyMap()
        _doubtfulQuestions.value = emptySet()
        _violationsCount.value = 0
        _showViolationDialog.value = false
        _showQuestionNavigator.value = false
        _showSubmitConfirmDialog.value = false

        examStartTimeMillis = System.currentTimeMillis()
        val totalSeconds = pkg.durationMinutes * 60L
        _remainingTimeSeconds.value = totalSeconds

        _currentScreen.value = Screen.ExamRoom(pkg.id)

        timerJob = viewModelScope.launch {
            while (_remainingTimeSeconds.value > 0) {
                delay(1000)
                _remainingTimeSeconds.value -= 1
            }
            // Auto submit when time runs out
            submitExam(isAutoTimeout = true)
        }
    }

    fun selectOption(questionIndex: Int, optionId: String) {
        val current = _userAnswers.value.toMutableMap()
        if (current[questionIndex] == optionId) {
            // Deselect if clicked again
            current.remove(questionIndex)
        } else {
            current[questionIndex] = optionId
        }
        _userAnswers.value = current
    }

    fun toggleDoubtful(questionIndex: Int) {
        val set = _doubtfulQuestions.value.toMutableSet()
        if (set.contains(questionIndex)) {
            set.remove(questionIndex)
        } else {
            set.add(questionIndex)
        }
        _doubtfulQuestions.value = set
    }

    fun goToQuestion(index: Int) {
        val total = _selectedPackage.value?.questions?.size ?: 0
        if (index in 0 until total) {
            _currentQuestionIndex.value = index
            _showQuestionNavigator.value = false
        }
    }

    fun nextQuestion() {
        val total = _selectedPackage.value?.questions?.size ?: 0
        if (_currentQuestionIndex.value < total - 1) {
            _currentQuestionIndex.value += 1
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun setFontSizeMode(mode: FontSizeMode) {
        _fontSizeMode.value = mode
    }

    fun toggleQuestionNavigator() {
        _showQuestionNavigator.value = !_showQuestionNavigator.value
    }

    fun closeQuestionNavigator() {
        _showQuestionNavigator.value = false
    }

    fun showSubmitDialog(show: Boolean) {
        _showSubmitConfirmDialog.value = show
    }

    // Anti-Cheat: User left app or window lost focus
    fun triggerSuspiciousAppSwitch() {
        if (_currentScreen.value is Screen.ExamRoom) {
            val newCount = _violationsCount.value + 1
            _violationsCount.value = newCount
            _showViolationDialog.value = true

            if (newCount >= 3) {
                // Max violations reached -> forced auto submit
                _showViolationDialog.value = false
                submitExam(isAutoTimeout = false)
            }
        }
    }

    fun dismissViolationDialog() {
        _showViolationDialog.value = false
    }

    fun submitExam(isAutoTimeout: Boolean = false) {
        timerJob?.cancel()
        _showSubmitConfirmDialog.value = false

        val pkg = _selectedPackage.value ?: return
        val questions = pkg.questions
        val answers = _userAnswers.value
        val doubts = _doubtfulQuestions.value

        var correctCount = 0
        var wrongCount = 0
        var unansweredCount = 0

        questions.forEachIndexed { index, q ->
            val userChoice = answers[index]
            if (userChoice == null) {
                unansweredCount++
            } else if (userChoice.equals(q.correctOptionId, ignoreCase = true)) {
                correctCount++
            } else {
                wrongCount++
            }
        }

        val totalQuestions = questions.size
        val percentage = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
        val isPassed = percentage >= pkg.passingScore

        val timeSpentSeconds = ((System.currentTimeMillis() - examStartTimeMillis) / 1000).coerceAtLeast(1)

        // Convert answers to JSON
        val answersJsonObj = JSONObject()
        answers.forEach { (k, v) -> answersJsonObj.put(k.toString(), v) }

        val doubtfulJsonObj = JSONObject()
        doubts.forEach { doubtfulJsonObj.put(it.toString(), true) }

        val entity = ExamResultEntity(
            packageId = pkg.id,
            examTitle = pkg.title,
            studentName = _studentProfile.value.fullName,
            studentIdNumber = _studentProfile.value.idNumber,
            score = percentage.toDouble(),
            maxScore = 100,
            percentage = percentage,
            correctCount = correctCount,
            wrongCount = wrongCount,
            unansweredCount = unansweredCount,
            doubtfulCount = doubts.size,
            timeSpentSeconds = timeSpentSeconds,
            timestamp = System.currentTimeMillis(),
            isPassed = isPassed,
            userAnswersJson = answersJsonObj.toString(),
            doubtfulNumbersJson = doubtfulJsonObj.toString(),
            violationsCount = _violationsCount.value
        )

        viewModelScope.launch {
            val insertedId = resultDao.insertResult(entity)
            val savedEntity = entity.copy(id = insertedId)
            _latestResult.value = savedEntity
            _currentScreen.value = Screen.Result(insertedId)
        }
    }

    fun loadResultDetail(resultId: Long) {
        viewModelScope.launch {
            val result = resultDao.getResultById(resultId)
            _latestResult.value = result
            result?.let {
                _selectedPackage.value = ExamDataProvider.getPackageById(it.packageId)
            }
        }
    }

    fun deleteResult(resultId: Long) {
        viewModelScope.launch {
            resultDao.deleteResult(resultId)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            resultDao.clearAll()
        }
    }
}
