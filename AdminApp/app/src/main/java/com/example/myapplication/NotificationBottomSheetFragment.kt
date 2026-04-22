package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.NotificationAdapter
import com.example.myapplication.databinding.FragmentNotificationBottomSheetBinding
import com.example.myapplication.model.NotificationModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomSheetBinding
    private lateinit var database: FirebaseDatabase
    private var notificationList = ArrayList<NotificationModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBottomSheetBinding.inflate(inflater, container, false)
        
        database = FirebaseDatabase.getInstance()
        
        binding.buttonBack.setOnClickListener {
            dismiss()
        }
        
        fetchNotifications()
        
        return binding.root
    }

    private fun fetchNotifications() {
        val notificationRef = database.reference.child("admin_notifications")
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
