package com.example.tienda.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tienda.data.model.Product
import java.io.Serializable

@Entity(tableName = "carts")
data class Cart (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val detailProduct: String
): Serializable

data class CheckoutCart(
    val id: Long,
    val cart: Product
)