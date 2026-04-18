package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.PendingOrderAdapter
import com.example.myapplication.databinding.ActivityPendingOrderBinding
import com.example.myapplication.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity() {
    private val binding: ActivityPendingOrderBinding by lazy{
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    
    private var listOfPendingOrders: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")

        binding.backButton.setOnClickListener{
            finish()
        }
        
        getPendingOrders()
    }
    
    private fun getPendingOrders() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfPendingOrders.clear()
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        if (!it.orderAccepted) {
                            listOfPendingOrders.add(it)
                        }
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PendingOrderActivity, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun setAdapter() {
        val adapter = PendingOrderAdapter(this, listOfPendingOrders)
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRecyclerView.adapter = adapter
    }
}