package com.ismt.babybuy.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ismt.babybuy.constants.AppConstants
import com.ismt.babybuy.databinding.ActivityLoginBinding
import com.ismt.babybuy.room.AppDatabase
import com.ismt.babybuy.utils.SharedPrefUtils
import com.ismt.babybuy.utils.ToastUtils
import com.ismt.babybuy.view.home.DashboardActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var db: AppDatabase

    public override fun onStart() {
        super.onStart()
        val sharedPref = SharedPrefUtils(this)
        if (sharedPref.getBoolean(AppConstants.KEY_IS_LOGGED_IN, false)) {
            val email = sharedPref.getString(AppConstants.KEY_EMAIL, "")
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra(AppConstants.KEY_EMAIL, email)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        db = AppDatabase.getInstance(this)

        viewBinding.btnLoginButton.setOnClickListener {
            val email = viewBinding.etEmail.text.toString().trim()
            val password = viewBinding.etPassword.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                viewBinding.etEmail.error = "Invalid email format"
                return@setOnClickListener
            } else {
                viewBinding.etEmail.error = null
            }

            if (password.isEmpty()) {
                viewBinding.etPassword.error = "Enter a password"
                return@setOnClickListener
            } else {
                viewBinding.etPassword.error = null
            }

            lifecycleScope.launch {
                loginUser(email, password)
            }
        }

        viewBinding.tvRegisterHere.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun loginUser(email: String, password: String) {
        startLoading()
        withContext(Dispatchers.IO) {
            val user = db.userDao().getValidUser(email, password)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    stopLoading()
                    val sharedPref = SharedPrefUtils(this@LoginActivity)
                    sharedPref.saveBoolean(AppConstants.KEY_IS_LOGGED_IN, true)
                    sharedPref.saveString(AppConstants.KEY_EMAIL, email)
                    ToastUtils.showToast(this@LoginActivity, "Logged in Successfully")
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra(AppConstants.KEY_EMAIL, email)
                    startActivity(intent)
                    finish()
                } else {
                    stopLoading()
                    ToastUtils.showToast(this@LoginActivity, "Invalid email or password")
                }
            }
        }
    }

    private fun startLoading() {
        viewBinding.pbLoginLoading.visibility = View.VISIBLE
        viewBinding.btnLoginButton.visibility = View.GONE
    }

    private fun stopLoading() {
        viewBinding.pbLoginLoading.visibility = View.GONE
        viewBinding.btnLoginButton.visibility = View.VISIBLE
    }
}
