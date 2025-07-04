package com.ismt.babybuy.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener

import com.ismt.babybuy.R
import com.ismt.babybuy.utils.ToastUtils


class ForgotPasswordActivity : AppCompatActivity() {//Initialization
    private val tag = "ForgotPasswordActivity"
    private lateinit var btnForgotPassword : Button
    private lateinit var etEmail : EditText
    private lateinit var pbForgotPassword : ProgressBar
    private lateinit var btnBack : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        //Bindings

        btnForgotPassword = findViewById(R.id.btn_resetPasswordButton)
        etEmail = findViewById(R.id.et_email)
        pbForgotPassword = findViewById(R.id.pb_forgotPasswordLoading)
        btnBack = findViewById(R.id.ib_back)

        // Forgot Password Onclick Behaviour
        btnForgotPassword.setOnClickListener {
            // Start Loading
            startLoading()
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Enter a valid Email"
                ToastUtils.showToast(this, "Enter a valid Email")
                stopLoading()
            } else {
               TODO()
            }
        }


        //Back button onclick
        btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity :: class.java)
            startActivity(intent)
            finish()
        }
    }

    //Start Loading
    private fun startLoading(){
        //Display Loading
        pbForgotPassword.visibility = View.VISIBLE
        //Hide ForgotPassword Button on loading
        btnForgotPassword.visibility = View.GONE
    }

    //Stop Loading
    private fun stopLoading(){
        //Hide Loading
        pbForgotPassword.visibility = View.GONE
        //Display ForgotPassword Button on loading
        btnForgotPassword.visibility = View.VISIBLE
    }
}