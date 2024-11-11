package com.example.tienda.network

import com.example.tienda.data.model.Product
import com.example.tienda.helper.Constant
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitAPI {
    @GET("${Constant.PRODUCT_PATH}/{path}")
    suspend fun getProducts(
        @Path("path") path: String,
        @Query("limit") query: String
    ): List<Product>

    @GET("${Constant.PRODUCT_PATH}/${Constant.CATEGORY_PATH}/{category}")
    suspend fun getProductsByCategory(
        @Path("category") path: String,
        @Query("limit") query: String
    ): List<Product>

    @GET("${Constant.PRODUCT_PATH}/${Constant.CATEGORIES_PATH}")
    suspend fun getCategory(): List<String>
}