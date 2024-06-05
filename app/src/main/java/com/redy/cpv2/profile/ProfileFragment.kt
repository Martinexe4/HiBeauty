package com.redy.cpv2.profile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.redy.cpv2.authentication.LoginActivity
import com.redy.cpv2.databinding.FragmentProfileBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.redy.cpv2.R

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = null
    private lateinit var storageReference: StorageReference
    private val PICK_IMAGE_REQUEST = 1  // Kode untuk memilih gambar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser = firebaseAuth.currentUser
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures")

        loadUserProfile()  // Menampilkan informasi profil pengguna

        binding.iconEditPhoto.setOnClickListener {
            choosePhoto()  // Memilih foto baru dari galeri
        }

        // Event listener untuk ikon edit nama
        binding.iconEditDisplayName.setOnClickListener {
            binding.txtDisplayName.visibility = View.GONE  // Sembunyikan TextView
            binding.edtDisplayName.visibility = View.VISIBLE  // Tampilkan EditText
            binding.btnEditProfile.visibility = View.VISIBLE  // Tampilkan tombol Save
            binding.btnCancelEdit.visibility = View.VISIBLE  // Tampilkan tombol Cancel
            binding.txtEmail.visibility = View.GONE  // Sembunyikan textview
        }

        // Event listener untuk menyimpan perubahan nama
        binding.btnEditProfile.setOnClickListener {
            editUserProfile()  // Panggil fungsi untuk memperbarui nama pengguna
            binding.txtDisplayName.visibility = View.VISIBLE  // Kembalikan TextView
            binding.edtDisplayName.visibility = View.GONE  // Sembunyikan EditText
            binding.btnEditProfile.visibility = View.GONE  // Sembunyikan tombol Save
            binding.btnCancelEdit.visibility = View.GONE  // Sembunyikan tombol Cancel
            binding.txtEmail.visibility = View.VISIBLE  // Tampilkan textview
        }

        // Event listener untuk membatalkan perubahan nama
        binding.btnCancelEdit.setOnClickListener {
            binding.txtDisplayName.visibility = View.VISIBLE  // Kembalikan TextView
            binding.edtDisplayName.visibility = View.GONE  // Sembunyikan EditText
            binding.btnEditProfile.visibility = View.GONE  // Sembunyikan tombol Save
            binding.btnCancelEdit.visibility = View.GONE  // Sembunyikan tombol Cancel
            binding.txtEmail.visibility = View.VISIBLE  // Tampilkan textview
        }

        binding.logOut.setOnClickListener {
            showLogoutConfirmationDialog()  // Tampilkan konfirmasi sebelum logout
        }

        binding.privacyPolicy.setOnClickListener {
            val intent = Intent(activity, PolicyActivity::class.java)
            startActivity(intent)
        }

        binding.historyAnalysis.setOnClickListener {
            val intent = Intent(activity, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.changeLanguage.setOnClickListener {
            val intent = Intent(activity, LanguageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun choosePhoto() {
        // Memeriksa izin sebelum membuka galeri
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            uploadPhotoToFirebase(imageUri)  // Mengunggah gambar ke Firebase Storage
        }
    }

    private fun uploadPhotoToFirebase(imageUri: Uri?) {
        if (imageUri == null) {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE  // Tampilkan ProgressBar

        val storageReference = FirebaseStorage.getInstance().getReference("profile_pictures")
        val fileName = "profile_${currentUser?.uid}.jpg"
        val fileRef = storageReference.child(fileName)  // Pastikan jalur benar

        fileRef.putFile(imageUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    binding.progressBar.visibility = View.GONE  // Sembunyikan ProgressBar setelah selesai
                    updateProfilePhoto(downloadUrl)  // Memperbarui foto profil di Firebase Auth
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE  // Sembunyikan ProgressBar setelah selesai
                Toast.makeText(requireContext(), "Upload failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun updateProfilePhoto(photoUrl: Uri) {
        val profileUpdates = userProfileChangeRequest {
            photoUri = photoUrl
        }

        currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Profile photo updated", Toast.LENGTH_SHORT).show()
                loadUserProfile()  // Memanggil fungsi untuk memperbarui tampilan
            } else {
                Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserProfile() {
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        currentUser?.let { user ->
            // Menggunakan Glide untuk memastikan gambar di-load dengan benar
            Glide.with(requireContext())
                .load(user.photoUrl ?: R.drawable.placeholder_image)  // Jika photoUrl null, gunakan default
                .placeholder(R.drawable.placeholder_image)  // Placeholder saat memuat
                .error(R.drawable.placeholder_image)  // Gambar saat terjadi kesalahan
                .circleCrop()  // Untuk memuat gambar dalam bentuk lingkaran
                .into(binding.imgProfile)

            binding.txtDisplayName.text = user.displayName ?: "No Name"
            binding.txtEmail.text = user.email ?: "No Email"
        }
    }

    private fun editUserProfile() {
        val newName = binding.edtDisplayName.text.toString()

        if (newName.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE  // Tampilkan ProgressBar

            val profileUpdates = userProfileChangeRequest {
                displayName = newName
            }

            currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE  // Sembunyikan ProgressBar setelah selesai

                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                    loadUserProfile()  // Perbarui tampilan profil untuk mencerminkan perubahan
                } else {
                    Toast.makeText(requireContext(), "Profile update failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                firebaseAuth.signOut()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}