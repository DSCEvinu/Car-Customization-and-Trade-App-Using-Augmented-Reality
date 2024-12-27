package com.example.carease.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.carease.R
import com.example.carease.data.model.Product
import com.example.carease.presentation.activity.VisualSearchActivity
import com.example.carease.presentation.adapter.CoverProductAdapter
import com.example.carease.presentation.adapter.ProductAdapter
import com.example.carease.presentation.adapter.SaleProductAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import android.util.Log

class HomeFragment : Fragment() {

    lateinit var coverRecView:RecyclerView
    lateinit var newRecView:RecyclerView
    lateinit var saleRecView:RecyclerView
    lateinit var coverProduct:ArrayList<Product>
    lateinit var newProduct:ArrayList<Product>
    lateinit var saleProduct:ArrayList<Product>
    lateinit var coverProductAdapter: CoverProductAdapter
    lateinit var newProductAdapter: ProductAdapter
    lateinit var saleProductAdapter: SaleProductAdapter
    lateinit var animationView: LottieAnimationView
    lateinit var newLayout:LinearLayout
    lateinit var saleLayout:LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        coverProduct = arrayListOf()
        newProduct = arrayListOf()
        saleProduct = arrayListOf()
        coverRecView = view.findViewById(R.id.coverRecView)
        newRecView = view.findViewById(R.id.newRecView)
        saleRecView = view.findViewById(R.id.saleRecView)
        newLayout = view.findViewById(R.id.newLayout)
        saleLayout = view.findViewById(R.id.saleLayout)
        animationView = view.findViewById(R.id.animationView)

        val visualSearchBtn_homePage:ImageView = view.findViewById(R.id.visualSearchBtn_homePage)

        hideLayout()
        setCoverData()
        setNewProductData()

        coverRecView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        coverRecView.setHasFixedSize(true)
        coverProductAdapter = CoverProductAdapter(activity as Context, coverProduct )
        coverRecView.adapter = coverProductAdapter
        coverProductAdapter.notifyDataSetChanged()

        newRecView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        newRecView.setHasFixedSize(true)
        newProductAdapter = ProductAdapter(newProduct, activity as Context )
        newRecView.adapter = newProductAdapter
        newProductAdapter.notifyDataSetChanged()

        saleRecView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        saleRecView.setHasFixedSize(true)
        saleProductAdapter = SaleProductAdapter(saleProduct, activity as Context )
        saleRecView.adapter = saleProductAdapter
        saleProductAdapter.notifyDataSetChanged()


        visualSearchBtn_homePage.setOnClickListener {
            startActivity(Intent(context, VisualSearchActivity::class.java))
        }
        showLayout()
        return view
    }

    private fun hideLayout(){
        animationView.playAnimation()
        animationView.loop(true)
        coverRecView.visibility = View.GONE
        newLayout.visibility = View.GONE
        saleLayout.visibility = View.GONE
        animationView.visibility = View.VISIBLE
    }

    private fun showLayout(){
        animationView.pauseAnimation()
        animationView.visibility = View.GONE
        coverRecView.visibility = View.VISIBLE
        newLayout.visibility = View.VISIBLE
        saleLayout.visibility = View.VISIBLE

        // Add logs
        Log.d("HomeFragmentData", "Cover Products: ${coverProduct.size}")
        Log.d("HomeFragmentData", "New Products: ${newProduct.size}")
        Log.d("HomeFragmentData", "Sale Products: ${saleProduct.size}")

    }

    fun getJsonData(context: Context, fileName: String): String? {
        var jsonString: String? = null
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            Log.d("JSON Data", "File content: $jsonString") // Log file content
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
        return jsonString
    }


    private fun setCoverData() {
        val jsonFileString = context?.let {
            getJsonData(it, "CoverProducts.json")
        }
        val gson = Gson()
        val listCoverType = object : TypeToken<List<Product>>() {}.type
        var coverD: List<Product> = gson.fromJson(jsonFileString, listCoverType)
        Log.d("JSON Data", "Cover Data: $coverD")
        coverD.forEachIndexed { idx, person ->
            coverProduct.add(person)
            saleProduct.add(person)
        }
    }

    private fun setNewProductData() {
        val jsonFileString = context?.let {
            getJsonData(it, "NewProducts.json")
        }
        val gson = Gson()
        val listCoverType = object : TypeToken<List<Product>>() {}.type
        var coverD: List<Product> = gson.fromJson(jsonFileString, listCoverType)
        Log.d("JSON Data", "New Data: $coverD")

        // Ensure products are added to the newProduct list
                newProduct.clear() // Clear any old data
        newProduct.addAll(coverD) // Add all the new products

        // Log the newProduct size to confirm
        Log.d("HomeFragmentData", "New Products: ${newProduct.size}")

        // Remove the forEach loop since addAll already adds the data
// coverD.forEachIndexed { idx, person ->
//     newProduct.add(person)
// }

    }
}