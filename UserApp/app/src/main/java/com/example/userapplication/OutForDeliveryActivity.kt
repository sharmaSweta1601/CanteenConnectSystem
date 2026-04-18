package com.example.userapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userapplication.adapter.DeliveryAdapter
import com.example.userapplication.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding:ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
     binding.backButton.setOnClickListener{
         finish()
     }
        val customerName= arrayListOf(
            "Mansi",
            "Sweta",
            "Mina"
        )
        val moneyStatus= arrayListOf(
            "recevied",
            "notrecevied",
            "pending"
        )
        val adapter=DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecyclerView.adapter=adapter
        binding.deliveryRecyclerView.layoutManager=LinearLayoutManager(this)

    }
}