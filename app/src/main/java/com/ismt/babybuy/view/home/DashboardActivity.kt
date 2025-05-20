package com.ismt.babybuy.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ismt.babybuy.R
import com.ismt.babybuy.databinding.ActivityDashboardBinding
import com.ismt.babybuy.view.home.fragment.HomeFragment
import com.ismt.babybuy.view.home.fragment.ProfileFragment
import com.ismt.babybuy.view.home.fragment.ShopFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityDashboardBinding
    private val exploreFragment = HomeFragment.newInstance()
    private val shopFragment = ShopFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setUpViews()
    }

    private fun setUpViews() {
        setupFragmentContainerView()
        setUpBottomNavigationView()
    }

    private fun setupFragmentContainerView() {
        loadFragmentInFcv(shopFragment) // Selects the initial page to show in the bottom nav
        viewBinding.bottomNavigationView.selectedItemId = R.id.product // Set the initial selected index of the bottom nav
    }

    private fun setUpBottomNavigationView() {
        viewBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragmentInFcv(exploreFragment)
                    true
                }
                R.id.product -> {
                    loadFragmentInFcv(shopFragment)
                    true
                }
                R.id.profile -> {
                    loadFragmentInFcv(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragmentInFcv(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(viewBinding.fcvDashboard.id, fragment)
            .commit()
    }
}
