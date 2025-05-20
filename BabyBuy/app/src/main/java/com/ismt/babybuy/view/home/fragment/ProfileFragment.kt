package com.ismt.babybuy.view.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ismt.babybuy.constants.AppConstants
import com.ismt.babybuy.databinding.FragmentProfileBinding
import com.ismt.babybuy.utils.SharedPrefUtils
import com.ismt.babybuy.view.LoginActivity

class ProfileFragment : Fragment() {
    private lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)

        // Retrieve the stored email and username from shared preferences
        val sharedPref = SharedPrefUtils(requireActivity())
        val email = sharedPref.getString(AppConstants.KEY_EMAIL)
        val username = sharedPref.getString(AppConstants.KEY_USERNAME)

        // Set the TextView values from the stored data
        profileBinding.tvEmail.text = email
        profileBinding.tvUsername.text = username
        profileBinding.tvUserEmail.text = email

        // Logout Button Behaviour
        profileBinding.btnLogout.setOnClickListener {
            startLoading()
            // Clear the login session
            sharedPref.saveBoolean(AppConstants.KEY_IS_LOGGED_IN, false)
            sharedPref.saveString(AppConstants.KEY_EMAIL, "")
            sharedPref.saveString(AppConstants.KEY_USERNAME, "")

            // Display a toast message indicating logout
            Toast.makeText(requireContext(), "You have been logged out", Toast.LENGTH_SHORT).show()

            // Redirect to LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return profileBinding.root
    }

    // Start Loading
    private fun startLoading() {
        // Display Loading
        profileBinding.pbLogoutLoading.visibility = View.VISIBLE
        // Hide logout Button on loading
        profileBinding.btnLogout.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
