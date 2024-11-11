package com.example.tienda.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tienda.data.CheckoutCart
import com.example.tienda.data.model.Product
import com.example.tienda.databinding.ItemCartBinding
import com.example.tienda.databinding.ItemFilterBinding
import com.example.tienda.databinding.ItemProductBinding
import com.google.gson.Gson

class CartAdapter(
    private val mContext: Context,
    private val carts: List<CheckoutCart>,
    private val onItemRemove: ((checkoutCart: CheckoutCart) -> Unit)
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(this::class.simpleName, "carts: ${Gson().toJson(carts)}")
        val checkoutCart: CheckoutCart = carts[position]
        with(holder) {
            Glide.with(mContext).load(checkoutCart.cart.image).into(binding.ivProduct)
            binding.tvTitle.text = checkoutCart.cart.title
            binding.tvPrice.text = "$ ${checkoutCart.cart.price}"
            binding.llCartItem.setOnLongClickListener {
                val alertDialog = AlertDialog.Builder(mContext)
                    .setMessage("Hapus barang dari keranjang?")
                    .setPositiveButton("Hapus") { _: DialogInterface?, _: Int ->
                        onItemRemove(checkoutCart)
                    }
                    .setNegativeButton("Batal") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                    .setCancelable(false)
                    .create()
                alertDialog.show()
                return@setOnLongClickListener false
            }
        }
    }

    override fun getItemCount(): Int {
        return carts.size
    }
}