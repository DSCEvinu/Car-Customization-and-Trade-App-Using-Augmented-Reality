package com.example.carease.presentation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carease.R
import com.example.carease.data.model.Product
import com.example.carease.presentation.activity.ProductDetailsActivity

class CoverProductAdapter(
    private val ctx: Context,
    private val coverProductList: ArrayList<Product>
) : RecyclerView.Adapter<CoverProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.cover_single, parent, false)
        return ViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = coverProductList[position]

        holder.apply {
            productNoteCover.text = product.productNote
            Glide.with(ctx)
                .load(product.productImage)
                .into(productImageCoverPage)

            productCheckCoverPage.setOnClickListener {
                goDetailsPage(position)
            }

            itemView.setOnClickListener {
                goDetailsPage(position)
            }
        }
    }

    override fun getItemCount(): Int = coverProductList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageCoverPage: ImageView = itemView.findViewById(R.id.productImage_coverPage)
        val productNoteCover: TextView = itemView.findViewById(R.id.productNoteCover)
        val productCheckCoverPage: Button = itemView.findViewById(R.id.productCheck_coverPage)
    }

    private fun goDetailsPage(position: Int) {
        val product = coverProductList[position]
        val intent = Intent(ctx, ProductDetailsActivity::class.java)
        intent.putExtra("name", product.productName)
        intent.putExtra("price", product.productPrice)
        intent.putExtra("image", product.productImage)
        ctx.startActivity(intent)
    }
}
