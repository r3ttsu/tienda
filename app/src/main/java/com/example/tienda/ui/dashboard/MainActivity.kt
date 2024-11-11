package com.example.tienda.ui.dashboard

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tienda.R
import com.example.tienda.adapter.CategoryAdapter
import com.example.tienda.adapter.ProductAdapter
import com.example.tienda.data.AppDatabase
import com.example.tienda.data.Cart
import com.example.tienda.data.dao.CartDao
import com.example.tienda.data.model.Product
import com.example.tienda.databinding.ActivityMainBinding
import com.example.tienda.handler.PrefSession
import com.example.tienda.helper.Constant
import com.example.tienda.network.ServiceRepository
import com.example.tienda.ui.LoginActivity
import com.example.tienda.ui.cart.CartActivity
import com.example.tienda.ui.productdetail.DetailProductActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: ServiceRepository
    private lateinit var factory: MainActivityViewModelFactory
    private lateinit var viewModel: MainActivityViewModel
    private var categories: MutableList<String> = mutableListOf()
    private lateinit var session: PrefSession
    private var myCart: MutableList<Cart> = mutableListOf()
    private lateinit var cartDao: CartDao
    private var doubleBackToExitPressedOnce = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        repository = ServiceRepository()
        factory = MainActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]
        session = PrefSession(this)
        val database = AppDatabase.getInstance(applicationContext)
        cartDao = database.cartDao()

        setupViewModel()
        binding.tvTitle.text = "Hello, ${session.getUsername()}"
        binding.fabCart.setOnClickListener(this)
        binding.btnFilter.setOnClickListener(this)
        viewModel.getAllProducts("20")
        viewModel.getAllCategory()
    }

    private fun setupViewModel() {
        viewModel.productListLiveData.observe(this) { it ->
            binding.rvProduct.setHasFixedSize(true)
            binding.rvProduct.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            val productAdapter = ProductAdapter(this, it) { onItemClicked(it) }
            binding.rvProduct.adapter = productAdapter
        }

        viewModel.productCategoryLiveData.observe(this) { productCategories ->
            productCategories.forEach {
                categories.add(it)
            }
            categories.add(0, "reset")
        }

        viewModel.errorLiveData.observe(this) {
            Toast.makeText(
                this,
                "Terjadi kesalahan: $it",
                Toast.LENGTH_SHORT
            ).show()
            Log.d(TAG, "ERROR: $it")
        }
    }

    private fun onItemClicked(detail: Product) {
        val intent = Intent(this, DetailProductActivity::class.java)
        intent.putExtra(Constant.INTENT_DATA, detail)
        startActivity(intent)
    }

    private fun onCategoryClicked(category: String, dialog: BottomSheetDialog) {
        if (category == "reset") {
            viewModel.getAllProducts("10")
        } else {
            viewModel.getProductByCategory(category, "10")
        }
        dialog.dismiss()
    }

    private fun goToCart() {
        val intent = Intent(this, CartActivity::class.java)
        intent.putExtra(Constant.INTENT_DATA, Gson().toJson(myCart))
        startActivity(intent)
    }

    private fun getMyCart() {
        myCart.clear()
        lifecycleScope.launch {
            val carts = getAllCarts()
            carts.forEach {
                if (it.username == session.getUsername()) {
                    myCart.add(it)
                }
            }
        }
    }

    private suspend fun getAllCarts(): List<Cart> {
        return withContext(Dispatchers.IO) {
            val users = cartDao.getAllCarts()
            Log.d("LoginActivity", "Retrieved users: $users")
            users
        }
    }

    private fun showProfile() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_profile, null)

        val dialog = BottomSheetDialog(this)
        dialog.behavior.maxHeight =
            (0.8 * Resources.getSystem().displayMetrics.heightPixels).toInt()
        val title = view.findViewById<TextView>(R.id.tv_username)
        title.text = session.getUsername()

        dialog.setOnCancelListener { obj: DialogInterface -> obj.dismiss() }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun openCategoryDialog() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)

        val dialog = BottomSheetDialog(this)
        val rvCategory = view.findViewById<RecyclerView>(R.id.rv_category)
        rvCategory.setHasFixedSize(true)
        rvCategory.layoutManager = LinearLayoutManager(this)
        val categoryAdapter = CategoryAdapter(categories) { onCategoryClicked(it, dialog) }
        rvCategory.adapter = categoryAdapter

        dialog.setOnCancelListener { obj: DialogInterface -> obj.dismiss() }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(a)
        } else {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.topbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                showProfile()
                return true
            }

            R.id.logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                session.clearSession()
                startActivity(intent)
                finish()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        getMyCart()
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.fabCart.id -> goToCart()
            binding.btnFilter.id -> openCategoryDialog()
        }
    }
}