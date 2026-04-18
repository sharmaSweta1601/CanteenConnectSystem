package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySendNotificationBinding
import com.example.myapplication.model.NotificationModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SendNotificationActivity : AppCompatActivity() {
    private val binding: ActivitySendNotificationBinding by lazy {
        ActivitySendNotificationBinding.inflate(layoutInflater)
    }
    
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        database = FirebaseDatabase.getInstance()

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.sendNotificationButton.setOnClickListener {
            val title = binding.notificationTitle.text.toString().trim()
            val message = binding.notificationMessage.text.toString().trim()

            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendNotificationToAllUsers(title, message)
        }
    }

    private fun sendNotificationToAllUsers(title: String, message: String) {
        val notification = NotificationModel(
            title = title,
            message = message,
            timestamp = System.currentTimeMillis()
        )

        val usersRef = database.reference.child("user")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0
                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key
                    if (userId != null) {
                        usersRef.child(userId).child("notifications").push().setValue(notification)
                        count++
                    }
                }
                Toast.makeText(this@SendNotificationActivity, "Offer sent to $count users!", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SendNotificationActivity, "Failed to send: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
