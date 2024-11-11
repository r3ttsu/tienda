package com.example.tienda.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tienda.data.CheckoutCart
import com.example.tienda.data.model.Product
import com.example.tienda.databinding.ItemFilterBinding
import com.example.tienda.databinding.ItemPesananBinding
import com.example.tienda.databinding.ItemProductBinding

class PesananAdapter(
    private val checkoutCarts: List<CheckoutCart>
) : RecyclerView.Adapter<PesananAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemPesananBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPesananBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkoutCart: CheckoutCart = checkoutCarts[position]
        with(holder) {
            binding.tvName.text = checkoutCart.cart.title
            binding.tvPrice.text = "$ ${checkoutCart.cart.price}"
        }
    }

    override fun getItemCount(): Int {
        return checkoutCarts.size
    }
}