package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.myapplication.databinding.ActivityAdminProfileBinding
import com.example.myapplication.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy{
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var adminId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        adminId = auth.currentUser?.uid ?: ""

        binding.backButton.setOnClickListener{
            finish()
        }
        binding.name.isEnabled=false
        binding.address.isEnabled=false
        binding.email.isEnabled=false
        binding.phone.isEnabled=false
        binding.password.isEnabled=false

        var isEnable=false
        binding.editButton.setOnClickListener{
            isEnable=!isEnable
            binding.name.isEnabled=isEnable
            binding.address.isEnabled=isEnable
            binding.email.isEnabled=isEnable
            binding.phone.isEnabled=isEnable
            binding.password.isEnabled=isEnable

            if(isEnable){
                binding.name.requestFocus()
            } else {
                saveUserData()
            }

        }

        retrieveUserData()
    }

    private fun retrieveUserData() {
        if (adminId.isEmpty()) return
        database.child("user").child(adminId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userProfile = snapshot.getValue(UserModel::class.java)
                    if (userProfile != null) {
                        binding.name.setText(userProfile.name)
                        binding.address.setText(userProfile.address)
                        binding.email.setText(userProfile.email)
                        binding.phone.setText(userProfile.phone)
                        binding.password.setText(userProfile.password)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminProfileActivity, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserData() {
        val updateName = binding.name.text.toString()
        val updateEmail = binding.email.text.toString()
        val updatePassword = binding.password.text.toString()
        val updatePhone = binding.phone.text.toString()
        val updateAddress = binding.address.text.toString()
        
        // We might not want to overwrite nameOfRestaurant, so let's fetch it first or update specific fields
        database.child("user").child(adminId).child("name").setValue(updateName)
        database.child("user").child(adminId).child("email").setValue(updateEmail)
        database.child("user").child(adminId).child("password").setValue(updatePassword)
        database.child("user").child(adminId).child("phone").setValue(updatePhone)
        database.child("user").child(adminId).child("address").setValue(updateAddress)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to Update Profile", Toast.LENGTH_SHORT).show()
            }
    }
}