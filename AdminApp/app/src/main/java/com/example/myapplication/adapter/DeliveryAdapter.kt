package com.example.myapplication.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.DeliveryItemBinding
import com.example.myapplication.model.OrderDetails
import com.google.firebase.database.FirebaseDatabase

class DeliveryAdapter(
    private val deliveryList: ArrayList<OrderDetails>
):RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding=DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = deliveryList.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val order = deliveryList[position]
            binding.apply {
                customerName.text = order.userName
                
                val paymentStatusText = if (order.paymentReceived) "received" else "notReceived"
                statusMoney.text = paymentStatusText
                
                val colorMap=mapOf(
                    "received" to Color.GREEN, "notReceived" to Color.RED, "Pending" to Color.GRAY
                )
                
                statusMoney.setTextColor(colorMap[paymentStatusText] ?: Color.RED)
                StatusColor.backgroundTintList = ColorStateList.valueOf(colorMap[paymentStatusText] ?: Color.GREEN)
                
                // Allow admin to mark payment as received
                root.setOnClickListener {
                    if (!order.paymentReceived && order.itemPushKey != null) {
                        FirebaseDatabase.getInstance().reference
                            .child("OrderDetails")
                            .child(order.itemPushKey!!)
                            .child("paymentReceived")
                            .setValue(true)
                            .addOnSuccessListener {
                                order.paymentReceived = true
                                notifyItemChanged(adapterPosition)
                                
                                // Send Notification
                                if (order.userId != null) {
                                    val notification = com.example.myapplication.model.NotificationModel(
                                        title = "Order Delivered",
                                        message = "Your order has been delivered successfully. Enjoy your meal!",
                                        timestamp = System.currentTimeMillis()
                                    )
                                    FirebaseDatabase.getInstance().reference
                                        .child("user").child(order.userId!!).child("notifications")
                                        .push().setValue(notification)
                                }
                            }
                    }
                }
            }
        }
    }
}