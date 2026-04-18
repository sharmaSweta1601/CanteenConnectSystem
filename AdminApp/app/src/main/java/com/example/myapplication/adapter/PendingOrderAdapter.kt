package com.example.myapplication.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.PendingOrderItemBinding
import com.example.myapplication.model.OrderDetails
import com.google.firebase.database.FirebaseDatabase

class PendingOrderAdapter(
    private val context: Context,
    private val pendingOrderList: ArrayList<OrderDetails>
):RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding=PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = pendingOrderList.size

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val order = pendingOrderList[position]
            binding.apply {
                customerName.text = order.userName
                pendingOrderQuantity.text = order.totalPrice // Or a sum of quantities
                
                if (!order.foodImages.isNullOrEmpty()) {
                    val uri = Uri.parse(order.foodImages!![0])
                    Glide.with(context).load(uri).into(orderFoodImage)
                }

                orderAcceptButton.apply {
                    if(!order.orderAccepted){
                        text="Accept"
                    }else{
                        text="Dispatch" // Handled by OutForDelivery
                    }
                    setOnClickListener{
                        if(!order.orderAccepted){
                            order.orderAccepted = true
                            text = "Dispatch"
                            showTost("Order is accepted")
                            updateOrderAcceptStatus(order.itemPushKey, order.userId)
                            pendingOrderList.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                        }
                    }
                }
            }
        }
        
        private fun updateOrderAcceptStatus(pushKey: String?, userId: String?) {
            if (pushKey != null) {
                val database = FirebaseDatabase.getInstance().reference
                database.child("OrderDetails").child(pushKey).child("orderAccepted").setValue(true)
                
                // Send Notification
                if (userId != null) {
                    val notification = com.example.myapplication.model.NotificationModel(
                        title = "Order Accepted",
                        message = "Your order is being prepared!",
                        timestamp = System.currentTimeMillis()
                    )
                    database.child("user").child(userId).child("notifications").push().setValue(notification)
                }
            }
        }
        
         private fun showTost(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }
    }
}