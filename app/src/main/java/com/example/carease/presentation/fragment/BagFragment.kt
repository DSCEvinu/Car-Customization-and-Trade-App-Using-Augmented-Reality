package com.example.carease.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.carease.R
import com.example.carease.data.local.room.CartViewModel
import com.example.carease.data.local.room.ProductEntity
import com.example.carease.presentation.adapter.CartAdapter
import com.example.carease.presentation.adapter.CartItemClickAdapter

class BagFragment : Fragment(), CartItemClickAdapter {

    private lateinit var cartRecView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var animationView: LottieAnimationView
    private lateinit var totalPriceBagFrag: TextView
    private lateinit var cartViewModel: CartViewModel
    private var items = ArrayList<ProductEntity>()
    private var totalPrice = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bag, container, false)
        initializeViews(view)
        setupRecyclerView()
        setupViewModel()
        return view
    }

    private fun initializeViews(view: View) {
        cartRecView = view.findViewById(R.id.cartRecView)
        animationView = view.findViewById(R.id.animationViewCartPage)
        totalPriceBagFrag = view.findViewById(R.id.totalPriceBagFrag)
        val bottomCartLayout: LinearLayout = view.findViewById(R.id.bottomCartLayout)
        val emptyBagMsgLayout: LinearLayout = view.findViewById(R.id.emptyBagMsgLayout)
        val myBagText: TextView = view.findViewById(R.id.MybagText)

        setupInitialState(bottomCartLayout, myBagText, emptyBagMsgLayout)
    }

    private fun setupInitialState(bottomCartLayout: LinearLayout, myBagText: TextView, emptyBagMsgLayout: LinearLayout) {
        animationView.apply {
            playAnimation()
            loop(true)
        }
        bottomCartLayout.visibility = View.GONE
        myBagText.visibility = View.GONE
        emptyBagMsgLayout.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(requireContext(), this)
        cartRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
    }

    private fun setupViewModel() {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        cartViewModel.allproducts.observe(viewLifecycleOwner) { list ->
            updateUI(list)
        }
    }

    private fun updateUI(list: List<ProductEntity>) {
        cartAdapter.updateList(list)
        items.clear()
        totalPrice = 0
        items.addAll(list)

        updateVisibility(list.isEmpty())
        updateTotalPrice()
    }

    private fun updateVisibility(isEmpty: Boolean) {
        view?.apply {
            findViewById<LinearLayout>(R.id.emptyBagMsgLayout).visibility = if (isEmpty) View.VISIBLE else View.GONE
            findViewById<LinearLayout>(R.id.bottomCartLayout).visibility = if (isEmpty) View.GONE else View.VISIBLE
            findViewById<TextView>(R.id.MybagText).visibility = if (isEmpty) View.GONE else View.VISIBLE
        }

        if (isEmpty) {
            animationView.playAnimation()
        } else {
            animationView.pauseAnimation()
        }
    }

    private fun updateTotalPrice() {
        totalPrice = items.sumOf { it.price }
        totalPriceBagFrag.text = "$$totalPrice"
    }

    override fun onItemDeleteClick(product: ProductEntity) {
        cartViewModel.deleteCart(product)
        Toast.makeText(context, "Removed From Bag", Toast.LENGTH_SHORT).show()
    }

    override fun onItemUpdateClick(product: ProductEntity) {
        cartViewModel.updateCart(product)
    }
}
