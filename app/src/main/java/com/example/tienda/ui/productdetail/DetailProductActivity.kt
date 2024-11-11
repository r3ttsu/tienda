package com.example.tienda.ui.productdetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tienda.R
import com.example.tienda.data.AppDatabase
import com.example.tienda.data.Cart
import com.example.tienda.data.dao.CartDao
import com.example.tienda.data.model.Product
import com.example.tienda.databinding.ActivityDetailProductBinding
import com.example.tienda.handler.PrefSession
import com.example.tienda.helper.Constant
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var data: Product
    private lateinit var cartDao: CartDao
    private lateinit var session: PrefSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getInstance(applicationContext)
        cartDao = database.cartDao()
        session = PrefSession(this)

        handleIntent()
        setupView()
        binding.btnBuy.setOnClickListener { onClickBuy() }
    }

    private fun onClickBuy() {
        val cart = session.getUsername()?.let {
            Cart(
                username = it,
                detailProduct = Gson().toJson(data)
            )
        }
        lifecycleScope.launch {
            if (cart != null) {
                addToCart(cart)
            }
            finish()
        }
        this.finish()
    }

    private fun handleIntent() {
        intent?.let {
            data = it.getSerializableExtra(Constant.INTENT_DATA) as Product
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        data.let {
            Glide.with(this).load(it.image).into(binding.ivProduct)
            binding.tvPrice.text = "$ ${it.price}"
            binding.tvName.text = it.title
            binding.tvRating.text = "${it.rating.rate} oleh ${it.rating.count} orang"
            binding.tvDetail.text = it.description
        }
    }

    private suspend fun addToCart(cart: Cart) {
        withContext(Dispatchers.IO) {
            cartDao.insertCarts(cart)
            Log.d("LoginActivity", "User inserted: $cart")
        }
    }
}