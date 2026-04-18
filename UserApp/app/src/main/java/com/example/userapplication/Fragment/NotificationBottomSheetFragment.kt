package com.example.userapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapplication.adapter.NotificationAdapter
import com.example.userapplication.databinding.FragmentNotificationBottomSheetBinding
import com.example.userapplication.model.NotificationModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomSheetBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var notificationList = ArrayList<NotificationModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBottomSheetBinding.inflate(inflater, container, false)
        
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        
        binding.buttonBack.setOnClickListener {
            dismiss()
        }
        
        fetchNotifications()
        
        return binding.root
    }

    private fun fetchNotifications() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val notificationRef = database.reference.child("user").child(userId).child("notifications")
            notificationRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    notificationList.clear()
                    for (notificationSnapshot in snapshot.children) {
                        val notification = notificationSnapshot.getValue(NotificationModel::class.java)
                        notification?.let {
                            notificationList.add(it)
                        }
                    }
                    notificationList.reverse() // Show newest first
                    val adapter = NotificationAdapter(notificationList)
                    binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.notificationRecyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to fetch notifications", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
