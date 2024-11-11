package com.example.tienda.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tienda.R
import com.example.tienda.data.AppDatabase
import com.example.tienda.data.User
import com.example.tienda.data.dao.UserDao
import com.example.tienda.databinding.ActivityLoginBinding
import com.example.tienda.handler.PrefSession
import com.example.tienda.ui.dashboard.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDao: UserDao
    private lateinit var allUsers: List<User>
    private lateinit var session: PrefSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getInstance(applicationContext)
        userDao = database.userDao()
        session = PrefSession(this)

        if (session.getLoginStatus()) {
            goToDashboard()
        }

        binding.btnLogin.setOnClickListener(this)
        binding.txtRegister.setOnClickListener(this)
    }

//    private suspend fun insertUser(user: User) {
//        withContext(Dispatchers.IO) {
//            userDao.insertUser(user)
//            Log.d("LoginActivity", "User inserted: $user")
//        }
//    }

    private suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            val users = userDao.getAllUsers()
            Log.d("LoginActivity", "Retrieved users: $users")
            users
        }
    }

    private suspend fun deleteUsers(users: List<User>) {
        withContext(Dispatchers.IO) {
            users.forEach { user ->
                userDao.deleteUser(user)
                Log.d("LoginActivity", "User deleted: $user")
            }
        }
    }

    private fun submitLogin() {
        val username = binding.etUsername.text.toString()
        val pord = binding.etPassword.text.toString()

        if (username != "" && pord != "") {
            if (allUsers.isEmpty()) {
                Toast.makeText(this, "Silakan registrasi terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            allUsers.forEach { user ->
                if (user.username == username && user.pord == pord) {
                    session.setLoginStatus(true)
                    session.setUsername(username)
                    goToDashboard()
                    return
                }
            }

            Toast.makeText(
                this,
                "Kami tidak menemukan akun anda. Silakan registrasi terlebih dahulu",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Toast.makeText(this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun goToDashboard() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> submitLogin()
            R.id.txt_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            allUsers = getAllUsers()
        }
    }
}