package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.myapplication.databinding.ActivityAdditemBinding
import com.example.myapplication.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdditemActivity : AppCompatActivity() {

    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding by lazy {
        ActivityAdditemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.AddItemButton.setOnClickListener {
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredint.text.toString().trim()

            if (foodName.isNotEmpty() && foodPrice.isNotEmpty()
                && foodDescription.isNotEmpty() && foodIngredient.isNotEmpty()
            ) {
                uploadData()
            } else {
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    // 🔥 CLOUDINARY UPLOAD + FIREBASE DB SAVE
    private fun uploadData() {

        if (foodImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val menuRef: DatabaseReference = database.getReference("menu")
        val newItemKey = menuRef.push().key ?: return

        MediaManager.get().upload(foodImageUri!!)
            .callback(object : UploadCallback {

                override fun onStart(requestId: String?) {
                    Toast.makeText(this@AdditemActivity, "Uploading image...", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {

                    val imageUrl = resultData["secure_url"].toString()

                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredient,
                        foodImage = imageUrl
                    )

                    menuRef.child(newItemKey).setValue(newItem)
                        .addOnSuccessListener {
                            Toast.makeText(this@AdditemActivity, "Item added successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@AdditemActivity, "Failed to save data", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Toast.makeText(this@AdditemActivity, "Image upload failed", Toast.LENGTH_SHORT).show()
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                foodImageUri = it
                binding.selectedImage.setImageURI(it)
            }
        }
}
