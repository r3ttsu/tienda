package com.example.tienda.network

import com.example.tienda.data.model.Product

class ServiceRepository {
    private val apiService = RetrofitClient.retrofitAPI

    suspend fun getProducts(path: String, limit: String): List<Product> {
        return apiService.getProducts(path, limit)
    }

    suspend fun getProductsByCategory(path: String, limit: String): List<Product> {
        return apiService.getProductsByCategory(path, limit)
    }

    suspend fun getCategory(): List<String> {
        return apiService.getCategory()
    }
}