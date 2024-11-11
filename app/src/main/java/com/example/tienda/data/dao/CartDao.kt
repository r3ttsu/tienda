package com.example.tienda.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tienda.data.Cart
import com.example.tienda.data.User

@Dao
interface CartDao {
    @Query("SELECT * FROM carts")
    suspend fun getAllCarts(): List<Cart>

    @Insert
    suspend fun insertCarts(cart: Cart)

    @Delete
    suspend fun deleteCarts(cart: Cart)

    @Query("DELETE FROM carts WHERE id == :cartId")
    suspend fun deleteCart(cartId: Long)
}