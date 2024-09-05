package com.example.imageorganizer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imageorganizer.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.fabChangeProfilePic.setOnClickListener {
            openImagePicker()
        }

        binding.verifyButton.setOnClickListener {
            registerUserWithEmailAndPassword()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            binding.profileImageView.setImageURI(selectedImageUri)
        }
    }

    private fun registerUserWithEmailAndPassword() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val branch = binding.branchEditText.text.toString().trim()
        val rollNumber = binding.rollNumberEditText.text.toString().trim()

        if (name.isEmpty() || branch.isEmpty() || rollNumber.isEmpty() || email.isEmpty() || password.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.endsWith("@iiitu.ac.in")) {
            binding.emailInputLayout.error = "Please use a valid iiitu.ac.in email"
            return
        }

        // Create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Upload profile image
                    uploadProfileImage()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadProfileImage() {
        val userId = auth.currentUser?.uid ?: return
        val storageRef: StorageReference = storage.reference.child("profile_images/$userId.jpg")

        selectedImageUri?.let { uri ->
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                        saveUserToFirestore(imageUrl.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("SignUpActivity", "Image upload failed: ${e.message}")
                    Toast.makeText(this, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(this, "Error: selectedImageUri is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserToFirestore(imageUrl: String) {
        val user = hashMapOf(
            "name" to binding.nameEditText.text.toString(),
            "branch" to binding.branchEditText.text.toString(),
            "rollNumber" to binding.rollNumberEditText.text.toString(),
            "email" to binding.emailEditText.text.toString(),
            "profileImage" to imageUrl // Save the image URL
        )

        firestore.collection("picxcel").document("User") // Updated path
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User data saved successfully.", Toast.LENGTH_SHORT).show()
                navigateToHomeActivity()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Close the SignUpActivity
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICKER = 1001
    }
}
