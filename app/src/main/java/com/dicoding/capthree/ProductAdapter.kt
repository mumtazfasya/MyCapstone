package com.dicoding.capthree

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.capthree.databinding.ItemProductBinding
import com.dicoding.capthree.data.Product


class ProductAdapter(val listProduct: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder (val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = listProduct.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val dataProduct = listProduct[position]
        Glide.with(holder.itemView.context)
            .load("https://storage.googleapis.com/easychicb/public/images/"+dataProduct.image)
            .into(holder.binding.itemPhoto)
        holder.binding.itemName.text = dataProduct.name

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.PRODUCT, dataProduct)
            holder.itemView.context.startActivity(intentDetail)
        }
    }


}