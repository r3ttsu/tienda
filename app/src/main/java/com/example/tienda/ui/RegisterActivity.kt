package com.example.tienda.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tienda.data.AppDatabase
import com.example.tienda.data.User
import com.example.tienda.data.dao.UserDao
import com.example.tienda.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userDao: UserDao
    private lateinit var allUsers: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getInstance(applicationContext)
        userDao = database.userDao()

        binding.btnRegister.setOnClickListener { registerAccount() }
        lifecycleScope.launch {
            allUsers = getAllUsers()
        }
    }

    private fun registerAccount() {
        val userName = binding.etUsername.text.toString()
        val pord = binding.etPassword.text.toString()
        val confirmPord = binding.etConfirmPassword.text.toString()

        if (userName != "" && pord != "" && confirmPord != "") {
            if (pord != confirmPord) {
                Toast.makeText(this, "Konfirmasi password salah", Toast.LENGTH_SHORT).show()
            } else {
                allUsers.forEach { user ->
                    if (user.username == userName) {
                        Toast.makeText(this, "Username sudah digunakan", Toast.LENGTH_SHORT).show()
                        return
                    }
                }

                val user = User(
                    username = userName,
                    pord = pord
                )
                lifecycleScope.launch {
                    insertUser(user)
                    finish()
                }
            }
        } else {
            Toast.makeText(
                this,
                "Silakan periksa kembali username, password dan konfirmasi password anda",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user)
            Log.d("RegisterActivity", "User inserted: $user")
        }
    }

    private suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            val users = userDao.getAllUsers()
            Log.d("RegisterActivity", "Retrieved users: $users")
            users
        }
    }
}