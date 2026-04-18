package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.model.UserModel
import com.example.myapplication.databinding.ActivitySignUpBinding

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

//import com.google.firebase.Firebase
//import com.google.firebase.auth.*
//import com.google.firebase.auth.FirebaseAuth



//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var nameOfRestaurant:String
    private lateinit var database: DatabaseReference
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        //initalization firebase auth
         auth= Firebase.auth
        database = Firebase.database.reference


        binding.createUserButton.setOnClickListener {


            userName =binding.name.text.toString().trim()
            nameOfRestaurant =binding.restaurantName.text.toString().trim()
            email =binding.emailOrPhone.text.toString().trim()
            password =binding.password.text.toString().trim()

            if(userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this,"please enter valid required",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
            if (task.isSuccessful){
                Toast.makeText(this,"acoount created",Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"acoount failed",Toast.LENGTH_SHORT).show()
                Log.d("Account","createaccount:Failure",task.exception)
            }

        }
    }
//save data into database
    private fun saveUserData() {
        userName =binding.name.text.toString().trim()
        nameOfRestaurant =binding.restaurantName.text.toString().trim()
        email =binding.emailOrPhone.text.toString().trim()
        password =binding.password.text.toString().trim()
        val user=UserModel(userName,nameOfRestaurant,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
    //save user data Firebase database
        database.child("user").child(userId).setValue(user)
    }
}