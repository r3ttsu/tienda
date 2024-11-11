package com.example.tienda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.inject.Inject
import javax.inject.Singleton

data class Product(
    @field:Expose @field:SerializedName("id") var id: Int,
    @field:Expose @field:SerializedName("title") var title: String,
    @field:Expose @field:SerializedName("price") var price: Double,
    @field:Expose @field:SerializedName("category") var category: String,
    @field:Expose @field:SerializedName("description") var description: String,
    @field:Expose @field:SerializedName("image") var image: String,
    @field:Expose @field:SerializedName("rating") var rating: Rating,
): Serializable

data class Rating(
    @field:Expose @field:SerializedName("rate") var rate: Double,
    @field:Expose @field:SerializedName("count") var count: Int,
): Serializable