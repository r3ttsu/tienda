package com.example.tienda.ui.cart

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tienda.R
import com.example.tienda.adapter.CartAdapter
import com.example.tienda.adapter.CategoryAdapter
import com.example.tienda.adapter.PesananAdapter
import com.example.tienda.data.AppDatabase
import com.example.tienda.data.Cart
import com.example.tienda.data.CheckoutCart
import com.example.tienda.data.User
import com.example.tienda.data.dao.CartDao
import com.example.tienda.data.model.Product
import com.example.tienda.databinding.ActivityCartBinding
import com.example.tienda.helper.Constant
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private var carts: MutableList<CheckoutCart> = mutableListOf()
    private lateinit var cartDao: CartDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getInstance(applicationContext)
        cartDao = database.cartDao()

        handleIntent()
        setupView()
        binding.btnCheckout.setOnClickListener { checkoutCart() }
    }

    private fun checkoutCart() {
        openConfirmationDialog()
        carts.forEach {
            lifecycleScope.launch {
                removeCart(it.id)
            }
        }
        Handler().postDelayed({ this.finish() }, 2000)
    }

    private fun openConfirmationDialog() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_confirmation, null)

        val dialog = BottomSheetDialog(this)
        val rvPesanan = view.findViewById<RecyclerView>(R.id.rv_pesanan)
        rvPesanan.setHasFixedSize(true)
        rvPesanan.layoutManager = LinearLayoutManager(this)
        val pesananAdapter = PesananAdapter(carts)
        rvPesanan.adapter = pesananAdapter

        dialog.setOnCancelListener { obj: DialogInterface -> obj.dismiss() }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun handleIntent() {
        if (intent.hasExtra(Constant.INTENT_DATA)) {
            val intentData = intent.getStringExtra(Constant.INTENT_DATA)
            val listType = object : TypeToken<MutableList<Cart>>() {}.type
            val productType = object : TypeToken<Product>() {}.type
            Log.d(this::class.simpleName, "intentData: ${Gson().toJson(intentData)}")
            intentData.let {
                val cartList: MutableList<Cart> = Gson().fromJson(it, listType)
                for (cart in cartList) {
                    println("id: ${cart.id}, username: ${cart.username}, detailProduct: ${cart.detailProduct}")
                    carts.add(
                        CheckoutCart(
                            cart.id,
                            Gson().fromJson(cart.detailProduct, productType)
                        )
                    )
                }
            }
//            cart = Gson().fromJson(intentData, List<Cart>)
        }
    }

    private fun setupView() {
        binding.rvCart.setHasFixedSize(true)
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        val cartAdapter = CartAdapter(this, carts) { onItemClear(it) }
        binding.rvCart.adapter = cartAdapter
    }

    private fun onItemClear(checkoutCart: CheckoutCart) {
        carts.forEach {
            if (it.id == checkoutCart.id) {
                lifecycleScope.launch {
                    removeCart(it.id)
                }
                this.finish()
            }
        }
    }

    private suspend fun removeCart(id: Long) {
        withContext(Dispatchers.IO) {
            cartDao.deleteCart(id)
        }
    }
}