package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityCreateUserBinding
import com.example.myapplication.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CreateUserActivity : AppCompatActivity() {
    private val binding : ActivityCreateUserBinding by lazy{
        ActivityCreateUserBinding.inflate(layoutInflater)
    }
    
    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        auth = FirebaseAuth.getInstance()
        
        binding.backButton.setOnClickListener{
            finish()
        }

        binding.createUserButton.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create user in Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val userId = authResult.user?.uid
                    if (userId != null) {
                        val userModel = UserModel(
                            name = name,
                            email = email,
                            password = password,
                            nameOfRestaurant = "Admin" // default or add an edittext for it
                        )
                        FirebaseDatabase.getInstance().reference
                            .child("user")
                            .child(userId)
                            .setValue(userModel)
                            .addOnSuccessListener {
                                Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to create user: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}