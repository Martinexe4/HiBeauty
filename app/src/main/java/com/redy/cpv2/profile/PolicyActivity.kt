package com.redy.cpv2.profile


import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.redy.cpv2.R

class PolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        // Inisialisasi TextView dari layout
        val privacyPolicyContentTextView: TextView = findViewById(R.id.privacyPolicyContentTextView)
        val closeButton: ImageButton = findViewById(R.id.closeButton)

        // Mengisi TextView dengan kebijakan privasi
        val privacyPolicyText = """
            Kebijakan Privasi
            
            Terakhir diperbarui: 28 Mei 2024
            
            1. Pengantar
            Selamat datang di Hi Beauty. Privasi Anda sangat penting bagi kami. Kebijakan privasi ini menjelaskan bagaimana kami mengumpulkan, menggunakan, dan melindungi informasi pribadi Anda saat Anda menggunakan aplikasi kami.
            
            2. Informasi yang Kami Kumpulkan
            Kami mengumpulkan beberapa jenis informasi untuk menyediakan dan meningkatkan layanan kami kepada Anda, termasuk:
            
            - Informasi Pribadi: Nama, alamat email, nomor telepon, dan informasi lainnya yang Anda berikan secara sukarela.
            - Informasi Penggunaan: Data tentang bagaimana Anda berinteraksi dengan aplikasi kami, termasuk alamat IP, jenis perangkat, sistem operasi, dan aktivitas penggunaan.
            - Informasi Lokasi: Data lokasi geografis yang tepat jika Anda memberikan izin melalui pengaturan perangkat Anda.
            
            3. Penggunaan Informasi
            Informasi yang kami kumpulkan digunakan untuk berbagai tujuan, termasuk:
            
            - Menyediakan dan memelihara layanan aplikasi.
            - Memperbaiki dan mempersonalisasi pengalaman pengguna.
            - Mengelola akun Anda dan memberikan dukungan pelanggan.
            - Mengirimkan pembaruan, promosi, dan informasi lain yang relevan.
            - Menganalisis penggunaan aplikasi untuk meningkatkan layanan kami.
            
            4. Pembagian Informasi
            Kami tidak akan membagikan informasi pribadi Anda kepada pihak ketiga kecuali dalam keadaan berikut:
            
            - Dengan persetujuan Anda.
            - Untuk mematuhi kewajiban hukum atau melindungi hak dan kepentingan kami.
            - Dengan penyedia layanan pihak ketiga yang membantu kami dalam menjalankan aplikasi, dengan syarat mereka menjaga kerahasiaan informasi Anda.
            - Dalam kaitannya dengan merger, akuisisi, atau penjualan aset, dengan ketentuan bahwa pihak ketiga tersebut sepakat untuk mematuhi kebijakan privasi ini.
            
            5. Keamanan Informasi
            Kami mengambil langkah-langkah keamanan yang wajar untuk melindungi informasi Anda dari akses, penggunaan, atau pengungkapan yang tidak sah. Namun, tidak ada metode transmisi data melalui internet atau metode penyimpanan elektronik yang 100% aman, sehingga kami tidak dapat menjamin keamanan absolut.
            
            6. Hak Pengguna
            Anda memiliki hak untuk:
            
            - Mengakses, memperbarui, atau menghapus informasi pribadi Anda yang kami miliki.
            - Menarik persetujuan yang Anda berikan sebelumnya terkait penggunaan informasi pribadi Anda.
            - Mengajukan keluhan kepada otoritas perlindungan data terkait jika Anda merasa hak privasi Anda dilanggar.
            
            7. Perubahan Kebijakan Privasi
            Kami dapat memperbarui kebijakan privasi ini dari waktu ke waktu. Setiap perubahan akan diberitahukan melalui aplikasi atau metode lain yang sesuai sebelum perubahan berlaku. Anda dianjurkan untuk meninjau kebijakan privasi ini secara berkala.
            
            8. Hubungi Kami
            Jika Anda memiliki pertanyaan atau kekhawatiran tentang kebijakan privasi ini, jangan ragu untuk menghubungi kami di:
            
            [Informasi Kontak]
            Email: redy.wijaya24@gmail.com
            Linkedin: redy.wi
        """.trimIndent()

        privacyPolicyContentTextView.text = privacyPolicyText

        // Menutup activity saat tombol "X" diklik
        closeButton.setOnClickListener {
            finish()
        }
    }
}
