package com.example.carease.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.carease.R
import com.example.carease.data.model.Car
import com.example.carease.databinding.FragmentCarDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CarDetailsFragment : Fragment() {
    private var _binding: FragmentCarDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFirebase()
        loadCarDetails()
        setupClickListeners()
    }

    private fun setupFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    private fun loadCarDetails() {
        val carId = arguments?.getString("carId") ?: return
        database.reference.child("cars").child(carId)
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.getValue(Car::class.java)?.let { car ->
                    displayCarDetails(car)
                }
            }
    }

    private fun displayCarDetails(car: Car) {
        binding.apply {
            carNameText.text = car.name
            carPriceText.text = getString(R.string.car_price_per_day, car.price)
            Glide.with(requireContext())
                .load(car.imageUrl)
                .into(carImageView)
        }
    }

    private fun setupClickListeners() {
        binding.addToCartButton.setOnClickListener {
            addToCart()
        }

        binding.addToFavoritesButton.setOnClickListener {
            addToFavorites()
        }
    }

    private fun addToCart() {
        val userId = auth.currentUser?.uid ?: return
        val carId = arguments?.getString("carId") ?: return

        database.reference.child("carts").child(userId).child(carId)
            .setValue(true)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.added_to_cart, Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToFavorites() {
        val userId = auth.currentUser?.uid ?: return
        val carId = arguments?.getString("carId") ?: return

        database.reference.child("favorites").child(userId).child(carId)
            .setValue(true)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
