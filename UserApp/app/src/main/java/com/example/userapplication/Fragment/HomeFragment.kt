package com.example.userapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.userapplication.MenuBottomSheetFragment
import com.example.userapplication.R
import com.example.userapplication.databinding.FragmentHomeBinding
import com.example.userapplication.model.Menuitem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<Menuitem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }

        retrriveAndDisplayPopulerItems()

        return binding.root


    }

    private fun retrriveAndDisplayPopulerItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(Menuitem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                randomPopulerItems()
            }

            private fun randomPopulerItems() {
                val index = menuItems.indices.toList().shuffled()
                val numItemToShow = 6
                val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }

                setPopulerItemAdapter(subsetMenuItems)
            }

            private fun setPopulerItemAdapter(subsetMenuItems: List<Menuitem>) {

                val adaptar = com.example.userapplication.adapter.MenuAdapter(subsetMenuItems,requireContext())
                binding.popularRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                binding.popularRecyclerview.adapter = adaptar
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.menu1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.menu2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.menu3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "selected image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })





    }

}
