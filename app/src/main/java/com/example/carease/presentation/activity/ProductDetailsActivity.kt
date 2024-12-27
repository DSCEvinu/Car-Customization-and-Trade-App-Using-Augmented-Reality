package com.example.carease.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.carease.R
import com.example.carease.data.local.room.CartViewModel
import com.example.carease.data.local.room.ProductEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var cartViewModel: CartViewModel
    private lateinit var bookButton: Button
    private lateinit var addToCartButton: Button
    private lateinit var name: String
    private lateinit var priceValue: String
    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        initializeFirebase()
        initializeViews()
        setupCartViewModel()
        setDataToViews()
        setupClickListeners()
    }

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://carease-44bfe-default-rtdb.asia-southeast1.firebasedatabase.app/")
    }

    private fun initializeViews() {
        bookButton = findViewById(R.id.bookButton)
        addToCartButton = findViewById(R.id.addToCartButton)

        name = intent.getStringExtra("name") ?: "Car Name"
        priceValue = intent.getStringExtra("price") ?: "$0"
        imageUrl = intent.getStringExtra("image") ?: ""
    }

    private fun setupCartViewModel() {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
    }

    private fun setDataToViews() {
        findViewById<TextView>(R.id.carNameText).text = name
        findViewById<TextView>(R.id.priceText).text = priceValue

        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(findViewById(R.id.carImageView))
        }
    }

    private fun setupClickListeners() {
        bookButton.setOnClickListener { handleBooking() }
        addToCartButton.setOnClickListener { addToCart() }
    }

    private fun handleBooking() {
        val userId = auth.currentUser?.uid ?: run {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }

        val booking = hashMapOf(
            "carName" to name,
            "price" to priceValue,
            "imageUrl" to imageUrl,
            "bookingDate" to System.currentTimeMillis()
        )

        database.getReference("bookings")
            .child(userId)
            .push()
            .setValue(booking)
            .addOnSuccessListener {
                Toast.makeText(this, "Booking Successful!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Booking Failed. Try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToCart() {
        val price = priceValue.replace("$", "").toIntOrNull() ?: 0
        val product = ProductEntity(
            pId = System.currentTimeMillis().toString(),
            name = name,
            price = price,
            Image = imageUrl,
            qua = 1
        )

        cartViewModel.insert(product)
        Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show()
    }

}
