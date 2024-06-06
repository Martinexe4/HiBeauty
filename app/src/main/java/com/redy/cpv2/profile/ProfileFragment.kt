package com.redy.cpv2.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
    var binding: FragmentProfileBinding? = null // Hapus underscore dan kata kunci private agar dapat diakses dari luar kelas
    lateinit var sharedPreferences: SharedPreferences
    private val PICK_IMAGE_REQUEST = 1

    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        loadUserProfile() // Tambahkan ini agar data profil dimuat saat tampilan dibuat

        binding?.iconEditPhoto?.setOnClickListener {
            choosePhoto()
        }

        binding?.logOut?.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding?.privacyPolicy?.setOnClickListener {
            val intent = Intent(activity, PolicyActivity::class.java)
            startActivity(intent)
        }

        binding?.infoUser?.setOnClickListener {
            val intent = Intent(activity, InfoUserActivity::class.java)
            startActivity(intent)
        }

        binding?.historyAnalysis?.setOnClickListener {
            val intent = Intent(activity, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding?.changeLanguage?.setOnClickListener {
            val intent = Intent(activity, LanguageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun choosePhoto() {
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
            savePhotoToSharedPreferences(imageUri)
        }
    }

    private fun savePhotoToSharedPreferences(imageUri: Uri?) {
        imageUri?.let { uri ->
            val editor = sharedPreferences.edit()
            editor.putString("profile_photo", uri.toString())
            editor.apply()
            loadUserProfile()
        }
    }

    private fun loadUserProfile() {
        val profilePhotoUri = sharedPreferences.getString("profile_photo", null)
        profilePhotoUri?.let { uri ->
            Glide.with(requireContext())
                .load(Uri.parse(uri))
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .circleCrop()
                .into(binding?.imgProfile!!)
        }
        val displayName = sharedPreferences.getString("name", "")
        Log.d("ProfileFragment", "Nama dari SharedPreferences: $displayName")
        binding?.txtDisplayName?.text = displayName
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
        binding = null
    }
}