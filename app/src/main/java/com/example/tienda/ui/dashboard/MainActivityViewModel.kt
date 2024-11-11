package com.example.tienda.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tienda.data.model.Product
import com.example.tienda.network.ServiceRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivityViewModel(private val serviceRepository: ServiceRepository) : ViewModel() {
    private val TAG = MainActivityViewModel::class.java.simpleName

    private val _productListLiveData = MutableLiveData<List<Product>>()
    val productListLiveData: LiveData<List<Product>> get() = _productListLiveData

    private val _productCategoryLiveData = MutableLiveData<List<String>>()
    val productCategoryLiveData: LiveData<List<String>> get() = _productCategoryLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun getAllProducts(limit: String) {
        viewModelScope.launch {
            try {
                val products = serviceRepository.getProducts("", limit)
                Log.d(TAG, "products: ${Gson().toJson(products)}")
                _productListLiveData.value = products
            } catch (e: Exception) {
                _errorLiveData.value = "error getAllProducts ${e.message}"
            }
        }
    }

    fun getAllCategory() {
        viewModelScope.launch {
            try {
                val categories = serviceRepository.getCategory()
                Log.d(TAG, "category: ${Gson().toJson(categories)}")
                _productCategoryLiveData.value = categories
            } catch (e: Exception) {
                _errorLiveData.value = "error getAllCategory: ${e.message}"
            }
        }
    }

    fun getProductByCategory(category: String, limit: String) {
        viewModelScope.launch {
            try {
                val categories =
                    serviceRepository.getProductsByCategory(category, limit)
                Log.d(TAG, "getProductByCategory: ${Gson().toJson(categories)}")
                _productListLiveData.value = categories
            } catch (e: Exception) {
                _errorLiveData.value = "error getProductByCategory: ${e.message}"
            }
        }
    }
}

class MainActivityViewModelFactory(private val serviceRepository: ServiceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainActivityViewModel(serviceRepository) as T
    }
}