package com.ismt.babybuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ismt.babybuy.constants.AppConstants
import com.ismt.babybuy.utils.SharedPrefUtils
import com.ismt.babybuy.view.LoginActivity
import com.ismt.babybuy.view.home.DashboardActivity


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Timer
        Handler().postDelayed({
            //Read From Shared Pref if user is logged in
            var sharedPref = SharedPrefUtils(this)
            val isLoggedIn = sharedPref.getBoolean(AppConstants.KEY_IS_LOGGED_IN)

            val intentLogin = Intent(this, LoginActivity::class.java)
            val intentHome = Intent(this, DashboardActivity::class.java)

            if(isLoggedIn){
                startActivity(intentHome)
            } else {
                startActivity(intentLogin)
            }
            finish()
        }, 2000)
    }
}