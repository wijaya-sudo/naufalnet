package com.example.data

import com.example.model.ExamOption
import com.example.model.ExamPackage
import com.example.model.Question

object ExamDataProvider {

    fun getAllPackages(): List<ExamPackage> = listOf(
        getUtbkPackage(),
        getCpnsPackage(),
        getAkmPackage(),
        getSciencePackage()
    )

    fun getPackageById(id: String): ExamPackage? {
        return getAllPackages().find { it.id == id }
    }

    private fun getUtbkPackage(): ExamPackage {
        return ExamPackage(
            id = "pkg_utbk_2026",
            title = "UTBK-SNBT: Tes Potensi Skolastik (TPS)",
            category = "UTBK-SNBT",
            durationMinutes = 20,
            token = "UTBK26",
            passingScore = 70,
            description = "Simulasi standar SNPMB BPPP meliputi Penalaran Umum, Pengetahuan Kuantitatif, Pemahaman Bacaan & Menulis.",
            iconType = "graduation",
            questions = listOf(
                Question(
                    id = "utbk_q1",
                    number = 1,
                    stimulus = "Semua siswa yang mengikuti bimbingan intensif lulus ujian dengan nilai memuaskan. Sebagian peserta ujian yang meraih beasiswa tidak mengikuti bimbingan intensif. Budi adalah peraih beasiswa bergengsi.",
                    question = "Berdasarkan premis di atas, simpulan yang PALING TEPAT mengenai Budi adalah...",
                    options = listOf(
                        ExamOption("A", "Budi pasti tidak lulus ujian dengan memuaskan."),
                        ExamOption("B", "Budi mungkin mengikuti bimbingan intensif dan lulus memuaskan."),
                        ExamOption("C", "Budi tidak mungkin mengikuti bimbingan intensif."),
                        ExamOption("D", "Semua peraih beasiswa seperti Budi pasti tidak lulus bimbingan."),
                        ExamOption("E", "Budi dipastikan lulus tanpa perlu mengikuti ujian.")
                    ),
                    correctOptionId = "B",
                    explanation = "Premis menyatakan: 'Sebagian peraih beasiswa tidak mengikuti bimbingan intensif', artinya ada sebagian peraih beasiswa yang mengikuti bimbingan intensif dan ada yang tidak. Budi peraih beasiswa, sehingga Budi mungkin mengikuti bimbingan intensif dan lulus dengan nilai memuaskan.",
                    categoryName = "Penalaran Umum"
                ),
                Question(
                    id = "utbk_q2",
                    number = 2,
                    stimulus = null,
                    question = "Diberikan barisan bilangan: 3, 5, 9, 17, 33, X. Nilai pengganti X yang konsisten dengan pola adalah...",
                    options = listOf(
                        ExamOption("A", "49"),
                        ExamOption("B", "57"),
                        ExamOption("C", "65"),
                        ExamOption("D", "67"),
                        ExamOption("E", "71")
                    ),
                    correctOptionId = "C",
                    explanation = "Pola selisih antar suku:\n5 - 3 = 2 (2^1)\n9 - 5 = 4 (2^2)\n17 - 9 = 8 (2^3)\n33 - 17 = 16 (2^4)\nMaka selisih berikutnya adalah 32 (2^5).\nSehingga X = 33 + 32 = 65.",
                    categoryName = "Pengetahuan Kuantitatif"
                ),
                Question(
                    id = "utbk_q3",
                    number = 3,
                    stimulus = "Perkembangan kecerdasan buatan (artificial intelligence) telah mentransformasi berbagai sektor industri di Indonesia. Namun, pengadopsian teknologi ini menghadapi kendala krusial, antara lain ketimpangan infrastruktur digital serta keterbatasan talenta yang menguasai pemodelan data tingkat lanjut. Oleh sebab itu, akselerasi kurikulum komputasi menjadi urgensi mendesak.",
                    question = "Gagasan pokok yang terkandung dalam paragraf tersebut adalah...",
                    options = listOf(
                        ExamOption("A", "Kelebihan utama pemodelan data dalam komputasi."),
                        ExamOption("B", "Transformasi industri oleh AI dan kendala krusial pengadopsiannya."),
                        ExamOption("C", "Ketiadaan talenta digital di seluruh pelosok Indonesia."),
                        ExamOption("D", "Penyebab kebangkrutan perusahaan teknologi modern."),
                        ExamOption("E", "Penolakan masyarakat terhadap perkembangan kurikulum baru.")
                    ),
                    correctOptionId = "B",
                    explanation = "Kalimat utama memaparkan transformasi oleh AI, kemudian diikuti kalimat penjelas mengenai kendala (infrastruktur & talenta). Jadi gagasan pokoknya adalah transformasi sektor industri oleh AI beserta kendala dalam penerapannya.",
                    categoryName = "Pemahaman Bacaan"
                ),
                Question(
                    id = "utbk_q4",
                    number = 4,
                    stimulus = null,
                    question = "Jika fungsi f(x) = 2x - 3 dan g(x) = x² + 1, maka nilai dari (g ∘ f)(2) adalah...",
                    options = listOf(
                        ExamOption("A", "1"),
                        ExamOption("B", "2"),
                        ExamOption("C", "5"),
                        ExamOption("D", "7"),
                        ExamOption("E", "9")
                    ),
                    correctOptionId = "B",
                    explanation = "Pertama cari f(2): f(2) = 2(2) - 3 = 4 - 3 = 1.\nKemudian masukkan ke fungsi g: g(f(2)) = g(1) = 1² + 1 = 1 + 1 = 2.",
                    categoryName = "Penalaran Matematika"
                ),
                Question(
                    id = "utbk_q5",
                    number = 5,
                    stimulus = null,
                    question = "Penggunaan tanda baca dan ejaan yang SESUAI dengan Pedoman Umum Ejaan Bahasa Indonesia (PUEBI/EYD V) terdapat pada kalimat...",
                    options = listOf(
                        ExamOption("A", "Ibu membeli: jeruk, apel dan semangka dipasar tradisional."),
                        ExamOption("B", "Seminar daring itu diikuti oleh 1.500 peserta dari Aceh sampai Papua."),
                        ExamOption("C", "Prof. Dr. Hendra S.T. M.Sc sedang memberikan kuliah umum."),
                        ExamOption("D", "Meskipun hujan lebat, tetapi acara festival tetap berlangsung meriah."),
                        ExamOption("E", "Kami memerlukan perabot: meja, kursi, dan lemari arsip.")
                    ),
                    correctOptionId = "B",
                    explanation = "Opsi B tepat karena penulisan angka ribuan (1.500) memakai tanda titik dan kapitalisasi nama geografis (Aceh, Papua) benar.\n(A salah karena tanda titik dua setelah kata kerja; C salah tanda koma gelar; D salah mubazir 'meskipun ... tetapi'; E salah titik dua setelah predikat tanpa pendahulu lengkap).",
                    categoryName = "Literasi Bahasa Indonesia"
                ),
                Question(
                    id = "utbk_q6",
                    number = 6,
                    stimulus = null,
                    question = "Sebuah toko pakaian memberikan diskon ganda 20% + 10% untuk semua jaket. Jika harga label sebuah jaket adalah Rp300.000, berapakah harga yang harus dibayar pembeli di kasir?",
                    options = listOf(
                        ExamOption("A", "Rp210.000"),
                        ExamOption("B", "Rp216.000"),
                        ExamOption("C", "Rp220.000"),
                        ExamOption("D", "Rp225.000"),
                        ExamOption("E", "Rp240.000")
                    ),
                    correctOptionId = "B",
                    explanation = "Diskon ganda dihitung bertahap:\nTahap 1: Diskon 20% dari Rp300.000 = Rp60.000. Harga menjadi Rp240.000.\nTahap 2: Diskon 10% dari Rp240.000 = Rp24.000.\nHarga akhir = Rp240.000 - Rp24.000 = Rp216.000.",
                    categoryName = "Pengetahuan Kuantitatif"
                ),
                Question(
                    id = "utbk_q7",
                    number = 7,
                    stimulus = "Enam orang (P, Q, R, S, T, U) duduk melingkar mengelilingi meja bundar. P duduk berhadapan langsung dengan R. S duduk di sebelah kanan P. T tidak boleh duduk bersebelahan dengan R.",
                    question = "Siapakah yang PASTI duduk tepat di sebelah kiri P?",
                    options = listOf(
                        ExamOption("A", "Q"),
                        ExamOption("B", "T"),
                        ExamOption("C", "U"),
                        ExamOption("D", "R"),
                        ExamOption("E", "Tidak dapat ditentukan antara Q atau U")
                    ),
                    correctOptionId = "B",
                    explanation = "Posisi meja bundar 6 orang: Jika P di bawah (arah jam 6) dan R di atas (arah jam 12). Sebelah kanan P adalah arah jam 5 (diduduki S). Sisa posisi jam 7 (sebelah kiri P), jam 9, dan jam 11 (sebelah R). Karena T tidak boleh bersebelahan dengan R (jam 11 dan jam 9 tidak boleh bagi T jika dekat R), maka T harus berada di posisi jam 7, yaitu tepat di sebelah kiri P.",
                    categoryName = "Penalaran Analitik"
                )
            )
        )
    }

    private fun getCpnsPackage(): ExamPackage {
        return ExamPackage(
            id = "pkg_cat_bkn_2026",
            title = "Simulasi CAT BKN - SKD CPNS & PPPK",
            category = "CAT BKN / CPNS",
            durationMinutes = 25,
            token = "SKD26",
            passingScore = 65,
            description = "Simulasi Seleksi Kompetensi Dasar sesuai standar BKN: TWK (Wawasan Kebangsaan), TIU (Intelegensia Umum), dan TKP.",
            iconType = "badge",
            questions = listOf(
                Question(
                    id = "skd_q1",
                    number = 1,
                    stimulus = null,
                    question = "Perumusan dasar negara Indonesia pertama kali dibahas secara mendalam dalam sidang BPUPKI pertama. Tokoh yang menyampaikan usulan lima asas dasar negara pada tanggal 1 Juni 1945 adalah...",
                    options = listOf(
                        ExamOption("A", "Prof. Dr. Soepomo"),
                        ExamOption("B", "Mr. Mohammad Yamin"),
                        ExamOption("C", "Ir. Soekarno"),
                        ExamOption("D", "Drs. Mohammad Hatta"),
                        ExamOption("E", "Radjiman Wedyodiningrat")
                    ),
                    correctOptionId = "C",
                    explanation = "Ir. Soekarno menyampaikan pidato rumusan lima asas dasar negara yang kemudian dinamai Pancasila pada tanggal 1 Juni 1945, yang kini diperingati sebagai Hari Lahir Pancasila.",
                    categoryName = "TWK (Pilar Negara)"
                ),
                Question(
                    id = "skd_q2",
                    number = 2,
                    stimulus = null,
                    question = "Lembaga negara hasil reformasi yang berwenang mengadili pada tingkat pertama dan terakhir yang putusannya bersifat final untuk menguji undang-undang terhadap UUD 1945 adalah...",
                    options = listOf(
                        ExamOption("A", "Mahkamah Agung (MA)"),
                        ExamOption("B", "Komisi Yudisial (KY)"),
                        ExamOption("C", "Dewan Perwakilan Daerah (DPD)"),
                        ExamOption("D", "Mahkamah Konstitusi (MK)"),
                        ExamOption("E", "Badan Pemeriksa Keuangan (BPK)")
                    ),
                    correctOptionId = "D",
                    explanation = "Pasal 24C ayat (1) UUD 1945 menegaskan Mahkamah Konstitusi (MK) berwenang mengadili pada tingkat pertama dan terakhir yang putusannya bersifat final untuk menguji undang-undang terhadap UUD 1945.",
                    categoryName = "TWK (UUD 1945)"
                ),
                Question(
                    id = "skd_q3",
                    number = 3,
                    stimulus = null,
                    question = "ANALOGI KATA:\nHAUS : AIR = GELAP : ...",
                    options = listOf(
                        ExamOption("A", "Malam"),
                        ExamOption("B", "Lampu"),
                        ExamOption("C", "Hitam"),
                        ExamOption("D", "Ruangan"),
                        ExamOption("E", "Takut")
                    ),
                    correctOptionId = "B",
                    explanation = "Hubungan analogi: Haus membutuhkan Air untuk menghilangkannya. Gelap membutuhkan Lampu/Cahaya untuk menghilangkannya.",
                    categoryName = "TIU (Analogi Verbal)"
                ),
                Question(
                    id = "skd_q4",
                    number = 4,
                    stimulus = null,
                    question = "DERET ANGKA:\n4, 7, 12, 19, 28, ...",
                    options = listOf(
                        ExamOption("A", "37"),
                        ExamOption("B", "39"),
                        ExamOption("C", "40"),
                        ExamOption("D", "41"),
                        ExamOption("E", "42")
                    ),
                    correctOptionId = "B",
                    explanation = "Pola deret penambahan bilangan ganjil berurutan:\n4 + 3 = 7\n7 + 5 = 12\n12 + 7 = 19\n19 + 9 = 28\n28 + 11 = 39.",
                    categoryName = "TIU (Deret Angka)"
                ),
                Question(
                    id = "skd_q5",
                    number = 5,
                    stimulus = "Semua pejabat ASN wajib melaporkan LHKPN tepat waktu. Sebagian orang yang wajib melaporkan LHKPN mendapatkan penghargaan integritas.",
                    question = "Simpulan yang paling valid adalah...",
                    options = listOf(
                        ExamOption("A", "Semua pejabat ASN pasti mendapatkan penghargaan integritas."),
                        ExamOption("B", "Sebagian pejabat ASN mendapatkan penghargaan integritas."),
                        ExamOption("C", "Tidak ada pejabat ASN yang mendapatkan penghargaan integritas."),
                        ExamOption("D", "Orang yang tidak melapor LHKPN pasti ASN teladan."),
                        ExamOption("E", "Hanya yang bukan ASN yang mendapat penghargaan integritas.")
                    ),
                    correctOptionId = "B",
                    explanation = "Semua ASN melapor LHKPN. Sebagian dari mereka yang melapor LHKPN mendapat penghargaan. Jadi, sebagian pejabat ASN mendapatkan penghargaan integritas.",
                    categoryName = "TIU (Silogisme)"
                ),
                Question(
                    id = "skd_q6",
                    number = 6,
                    stimulus = "Anda adalah petugas loket pelayanan publik. Menjelang jam istirahat siang, datang seorang warga lansia yang menempuh perjalanan jauh dan memerlukan legalisir dokumen mendesak untuk pengobatan.",
                    question = "Sikap profesional dan berorientasi pelayanan yang PALING TEPAT Anda ambil adalah...",
                    options = listOf(
                        ExamOption("A", "Menutup loket tepat waktu dan memintanya kembali setelah jam istirahat selesai."),
                        ExamOption("B", "Melayani warga lansia tersebut dengan ramah hingga tuntas sebelum Anda istirahat makan siang."),
                        ExamOption("C", "Menyuruh rekan kerja lain yang masih duduk untuk melayaninya sementara Anda pergi makan."),
                        ExamOption("D", "Memarahinya karena datang mepet waktu istirahat dinas."),
                        ExamOption("E", "Memberikan nomor antrean untuk hari kerja berikutnya.")
                    ),
                    correctOptionId = "B",
                    explanation = "Prinsip TKP Pelayanan Publik: Mengutamakan kepentingan masyarakat dengan tulus, empati terhadap kaum rentan (lansia), serta fleksibilitas pelayanan dalam koridor kedinasan.",
                    categoryName = "TKP (Pelayanan Publik)"
                )
            )
        )
    }

    private fun getAkmPackage(): ExamPackage {
        return ExamPackage(
            id = "pkg_akm_nasional",
            title = "Asesmen Nasional: AKM Literasi & Numerasi",
            category = "Asesmen Nasional",
            durationMinutes = 15,
            token = "AKM26",
            passingScore = 60,
            description = "Asesmen Kompetensi Minimum menguji literasi membaca teks informasi dan numerasi analisis data dunia nyata.",
            iconType = "chart",
            questions = listOf(
                Question(
                    id = "akm_q1",
                    number = 1,
                    stimulus = "Sebuah panel surya berkapasitas 400 Watt peak rata-rata mampu menghasilkan daya efektif selama 5 jam sinar matahari optimal per hari. Sebuah rumah tangga memasang 4 unit panel surya tersebut untuk menghemat konsumsi listrik PLN.",
                    question = "Berapakah total energi listrik (dalam kiloWatt-hour / kWh) yang dihasilkan oleh sistem panel surya tersebut dalam 30 hari?",
                    options = listOf(
                        ExamOption("A", "120 kWh"),
                        ExamOption("B", "240 kWh"),
                        ExamOption("C", "360 kWh"),
                        ExamOption("D", "480 kWh"),
                        ExamOption("E", "600 kWh")
                    ),
                    correctOptionId = "B",
                    explanation = "Daya total 4 panel = 4 x 400 Watt = 1.600 Watt = 1,6 kW.\nEnergi per hari = 1,6 kW x 5 jam = 8 kWh/hari.\nEnergi dalam 30 hari = 8 kWh x 30 = 240 kWh.",
                    categoryName = "Numerasi (Energi Terbarukan)"
                ),
                Question(
                    id = "akm_q2",
                    number = 2,
                    stimulus = "Program Bank Sampah Mandiri di Kelurahan Sukamaju mencatat penurunan volume sampah ke TPA sebesar 35% pada tahun pertama. Warga menukarkan sampah plastik bernilai ekonomis dengan poin tabungan sembako. Dampak positif ini menginspirasi 5 kelurahan tetangga untuk menduplikasi program serupa.",
                    question = "Berdasarkan stimulus di atas, tujuan utama penerapan model insentif poin tabungan sembako adalah...",
                    options = listOf(
                        ExamOption("A", "Menghapuskan keberadaan petugas kebersihan kota."),
                        ExamOption("B", "Mendorong partisipasi aktif warga dalam pemilahan sampah berbasis nilai manfaat ekonomi."),
                        ExamOption("C", "Menjual sembako mahal kepada warga kurang mampu."),
                        ExamOption("D", "Memperbanyak sampah plastik di lingkungan perumahan."),
                        ExamOption("E", "Menutup akses warga terhadap tempat pembuangan akhir.")
                    ),
                    correctOptionId = "B",
                    explanation = "Model insentif tabungan sembako memberikan imbal balik nyata sehingga memotivasi warga memilah sampah dari sumbernya demi manfaat ekonomi.",
                    categoryName = "Literasi Sosial Humaniora"
                ),
                Question(
                    id = "akm_q3",
                    number = 3,
                    stimulus = "Data hasil panen padi 4 desa: Desa A (45 ton), Desa B (60 ton), Desa C (75 ton), Desa D (50 ton). Target rata-rata panen per desa adalah 60 ton.",
                    question = "Pernyataan yang PALING BENAR berdasarkan data tersebut adalah...",
                    options = listOf(
                        ExamOption("A", "Rata-rata panen seluruh desa di bawah target target."),
                        ExamOption("B", "Rata-rata hasil panen keempat desa tepat 57,5 ton, belum mencapai target 60 ton."),
                        ExamOption("C", "Rata-rata hasil panen mencapai 65 ton."),
                        ExamOption("D", "Desa A menyumbang hasil panen terbesar."),
                        ExamOption("E", "Desa B dan C memiliki hasil panen yang identik.")
                    ),
                    correctOptionId = "B",
                    explanation = "Total panen = 45 + 60 + 75 + 50 = 230 ton.\nRata-rata = 230 / 4 = 57,5 ton.\nKarena 57,5 < 60, maka belum mencapai target rata-rata.",
                    categoryName = "Numerasi (Statistika Data)"
                )
            )
        )
    }

    private fun getSciencePackage(): ExamPackage {
        return ExamPackage(
            id = "pkg_sains_sma",
            title = "Ujian Akhir: Matematika & Sains Terpadu",
            category = "Ujian Sekolah",
            durationMinutes = 20,
            token = "SAINS26",
            passingScore = 70,
            description = "Ujian terpadu mata pelajaran Matematika, Fisika, dan Kimia untuk evaluasi kelulusan jenjang menengah.",
            iconType = "science",
            questions = listOf(
                Question(
                    id = "sci_q1",
                    number = 1,
                    stimulus = null,
                    question = "Sebuah mobil bermassa 1.000 kg bergerak dengan kecepatan 20 m/s. Pengemudi menginjak rem hingga mobil berhenti dalam jarak 40 meter. Berapakah besar gaya pengereman rata-rata yang bekerja pada mobil?",
                    options = listOf(
                        ExamOption("A", "2.000 N"),
                        ExamOption("B", "3.000 N"),
                        ExamOption("C", "5.000 N"),
                        ExamOption("D", "8.000 N"),
                        ExamOption("E", "10.000 N")
                    ),
                    correctOptionId = "C",
                    explanation = "Gunakan hubungan Usaha dan Perubahan Energi Kinetik (W = ΔEk):\nF x s = 1/2 m (v² - 0)\nF x 40 = 1/2 (1.000) (20²)\nF x 40 = 500 x 400 = 200.000\nF = 200.000 / 40 = 5.000 Newton.",
                    categoryName = "Fisika (Mekanika)"
                ),
                Question(
                    id = "sci_q2",
                    number = 2,
                    stimulus = null,
                    question = "Himpunan penyelesaian dari pertidaksamaan kuadrat x² - 5x + 6 ≤ 0 adalah...",
                    options = listOf(
                        ExamOption("A", "x ≤ 2 atau x ≥ 3"),
                        ExamOption("B", "2 ≤ x ≤ 3"),
                        ExamOption("C", "-3 ≤ x ≤ -2"),
                        ExamOption("D", "x ≤ -3 atau x ≥ -2"),
                        ExamOption("E", "-2 ≤ x ≤ 3")
                    ),
                    correctOptionId = "B",
                    explanation = "Faktorkan persamaan: (x - 2)(x - 3) ≤ 0.\nTitik pembuat nol: x = 2 dan x = 3.\nUji titik pada garis bilangan, nilai negatif (≤ 0) berada di antara kedua akar, yaitu 2 ≤ x ≤ 3.",
                    categoryName = "Matematika (Aljabar)"
                ),
                Question(
                    id = "sci_q3",
                    number = 3,
                    stimulus = null,
                    question = "Unsur dengan nomor atom 17 memiliki konfigurasi elektron (berdasarkan kulit utama Bohr) dan letak golongan dalam tabel periodik adalah...",
                    options = listOf(
                        ExamOption("A", "2, 8, 7 dan Golongan VIIA"),
                        ExamOption("B", "2, 8, 8 dan Golongan VIIIA"),
                        ExamOption("C", "2, 7, 8 dan Golongan IIA"),
                        ExamOption("D", "2, 8, 5 dan Golongan VA"),
                        ExamOption("E", "2, 10, 5 dan Golongan VIIB")
                    ),
                    correctOptionId = "A",
                    explanation = "Unsur nomor atom 17 (Klorin / Cl):\nKonfigurasi: 2, 8, 7.\nElektron valensi = 7, sehingga terletak pada Golongan VIIA (Halogen) Periode 3.",
                    categoryName = "Kimia (Struktur Atom)"
                )
            )
        )
    }
}
