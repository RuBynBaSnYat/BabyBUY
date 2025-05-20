package com.ismt.babybuy.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.ismt.babybuy.R
import com.ismt.babybuy.databinding.ActivityRegisterBinding
import com.ismt.babybuy.room.AppDatabase
import com.ismt.babybuy.room.User
import com.ismt.babybuy.utils.ToastUtils
import kotlin.concurrent.thread

class RegisterActivity : AppCompatActivity() {
    // Initialization
    private lateinit var viewBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Register Button OnClick Behavior
        viewBinding.btnRegisterButton.setOnClickListener {
            val name = viewBinding.etName.text.toString().trim()
            val email = viewBinding.etEmail.text.toString().trim()
            val password = viewBinding.etPassword.text.toString().trim()
            val confirmPassword = viewBinding.etConfirmPassword.text.toString().trim()

            var isValid = true

            if (name.isEmpty()) {
                viewBinding.etName.error = "Name is required"
                isValid = false
            } else {
                viewBinding.etName.error = null // Clear the error
            }

            if (email.isEmpty()) {
                viewBinding.etEmail.error = "Email is required"
                isValid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                viewBinding.etEmail.error = "Invalid email format"
                isValid = false
            } else {
                viewBinding.etEmail.error = null // Clear the error
            }

            if (!validatePassword(password)) {
                isValid = false
            }

            if (password.isNotEmpty() && password != confirmPassword) {
                viewBinding.etConfirmPassword.error = "Passwords do not match"
                isValid = false
            } else {
                viewBinding.etConfirmPassword.error = null // Clear the error
            }

            if (isValid) {
                // Register user if all inputs are valid
                registerUser(name, email, password)
            }
        }

        // Login Here OnClick Behavior
        viewBinding.tvLoginHere.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Validate Password
    private fun validatePassword(password: String): Boolean {
        val passwordTextInputLayout = viewBinding.passwordTextInputLayout

        if (password.length < 8) {
            passwordTextInputLayout.error = "Password must be at least 8 characters"
            return false
        }
        if (!password.matches(".*\\d.*".toRegex())) {
            passwordTextInputLayout.error = "Password must contain a digit"
            return false
        }
        if (!password.matches(".*[a-z].*".toRegex())) {
            passwordTextInputLayout.error = "Password must contain a lowercase letter"
            return false
        }
        if (!password.matches(".*[A-Z].*".toRegex())) {
            passwordTextInputLayout.error = "Password must contain an uppercase letter"
            return false
        }
        if (!password.matches(".*[@#\$%^&+=].*".toRegex())) {
            passwordTextInputLayout.error = "Password must contain a special character"
            return false
        }

        passwordTextInputLayout.error = null
        return true
    }

    // Clear input fields data
    private fun clearInputFields() {
        viewBinding.etName.text?.clear()
        viewBinding.etEmail.text?.clear()
        viewBinding.etPassword.text?.clear()
        viewBinding.etConfirmPassword.text?.clear()
    }

    // Register User in Room Database
    private fun registerUser(name: String, email: String, password: String) {
        startLoading()

        thread {
            try {
                val user = User(
                    userName = name,
                    email = email,
                    password = password,
                    contactNumber = "" // Add appropriate contact number if needed
                )

                val userDatabase = AppDatabase.getInstance(this)
                val userDao = userDatabase.userDao()

                // Check if the user already exists
                val existingUser = userDao.checkUserExist(email)
                if (existingUser == null) {
                    // Insert new user into database
                    userDao.insertUser(user)
                    runOnUiThread {
                        ToastUtils.showToast(this, "User Registered")
                        clearInputFields()
                        // Redirect to login screen
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        ToastUtils.showToast(this, "User already exists")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    ToastUtils.showToast(this, "Couldn't register user!")
                }
            } finally {
                runOnUiThread {
                    stopLoading()
                }
            }
        }
    }

    // Start Loading
    private fun startLoading() {
        runOnUiThread {
            viewBinding.pbRegisterLoading.visibility = View.VISIBLE
            viewBinding.btnRegisterButton.visibility = View.GONE
        }
    }

    // Stop Loading
    private fun stopLoading() {
        runOnUiThread {
            viewBinding.pbRegisterLoading.visibility = View.GONE
            viewBinding.btnRegisterButton.visibility = View.VISIBLE
        }
    }
}
