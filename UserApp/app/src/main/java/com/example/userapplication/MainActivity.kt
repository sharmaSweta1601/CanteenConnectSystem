package com.example.userapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import com.example.userapplication.databinding.ActivityMainBinding
import com.example.userapplication.model.Menuitem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var NavController = findNavController(R.id.fragmentContainerView)
        var bottomnav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomnav.setupWithNavController(NavController)

        seedMenuItems()
    }

    private fun seedMenuItems() {
        val database = FirebaseDatabase.getInstance()
        val foodRef = database.reference.child("menu")
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists() || snapshot.childrenCount < 5) {
                    val items = listOf(
                        Menuitem("Classic Burger", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd", "150Rs", "Juicy beef patty with cheese and veggies.", "Beef, Cheese, Lettuce, Tomato, Bun"),
                        Menuitem("Margherita Pizza", "https://images.unsplash.com/photo-1574071318508-1cdbab80d002", "299Rs", "Classic delight with 100% real mozzarella cheese.", "Dough, Tomato Sauce, Mozzarella, Basil"),
                        Menuitem("Pasta Alfredo", "https://images.unsplash.com/photo-1645112411341-6c4fd023714a", "250Rs", "Creamy and rich pasta with parmesan cheese.", "Pasta, Heavy Cream, Parmesan, Butter, Garlic"),
                        Menuitem("Chicken Biryani", "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8", "350Rs", "Aromatic basmati rice cooked with tender chicken.", "Basmati Rice, Chicken, Spices, Yogurt, Onions"),
                        Menuitem("French Fries", "https://images.unsplash.com/photo-1576107232684-1279f3908594", "99Rs", "Crispy golden french fries.", "Potatoes, Salt, Oil"),
                        Menuitem("Cold Coffee", "https://images.unsplash.com/photo-1517701550927-30cf0b63f52e", "120Rs", "Refreshing cold coffee with ice cream.", "Coffee, Milk, Sugar, Ice Cream"),
                        Menuitem("Veg Sandwich", "https://images.unsplash.com/photo-1528735602780-2552fd46c7af", "80Rs", "Healthy sandwich with fresh vegetables.", "Bread, Cucumber, Tomato, Onion, Butter"),
                        Menuitem("Chocolate Shake", "https://images.unsplash.com/photo-1572490122747-3968b75bf699", "140Rs", "Thick and creamy chocolate milkshake.", "Milk, Chocolate Syrup, Cocoa, Sugar"),
                        Menuitem("Paneer Tikka", "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8", "220Rs", "Grilled cottage cheese cubes marinated in spices.", "Paneer, Yogurt, Spices, Capsicum, Onion"),
                        Menuitem("Masala Dosa", "https://images.unsplash.com/photo-1589301760014-d929f39ce9b1", "110Rs", "Crispy crepe made from rice batter filled with potato masala.", "Rice batter, Potato, Onion, Spices"),
                        Menuitem("Caesar Salad", "https://images.unsplash.com/photo-1550304943-4f24f54ddde9", "180Rs", "Fresh romaine lettuce with Caesar dressing and croutons.", "Lettuce, Croutons, Parmesan, Dressing")
                    )

                    for (item in items) {
                        val key = foodRef.push().key
                        if (key != null) {
                            val map = HashMap<String, Any?>()
                            map["key"] = key
                            map["foodName"] = item.foodName
                            map["foodImage"] = item.foodImage
                            map["foodPrice"] = item.foodPrice
                            map["foodDescription"] = item.foodDescription
                            map["foodIngredient"] = item.foodIngredient
                            foodRef.child(key).setValue(map)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}