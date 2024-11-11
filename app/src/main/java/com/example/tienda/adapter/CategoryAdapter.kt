package com.example.tienda.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tienda.data.model.Product
import com.example.tienda.databinding.ItemFilterBinding
import com.example.tienda.databinding.ItemProductBinding

class CategoryAdapter(
    private val categories: List<String>,
    private val onCategoryClick: ((category: String) -> Unit)
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemFilterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category: String = categories[position]
        with(holder) {
            binding.tvCategory.text = category
            binding.tvCategory.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}