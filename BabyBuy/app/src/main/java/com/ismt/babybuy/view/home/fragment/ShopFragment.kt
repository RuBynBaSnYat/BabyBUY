package com.ismt.babybuy.view.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher

import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismt.babybuy.R
import com.ismt.babybuy.constants.AppConstants
import com.ismt.babybuy.databinding.FragmentMyItemsBinding

import com.ismt.babybuy.room.AppDatabase
import com.ismt.babybuy.room.Product
import com.ismt.babybuy.utils.ToastUtils
import com.ismt.babybuy.view.home.AddOrUpdateActivity
import com.ismt.babybuy.view.home.ProductDetailActivity
import com.ismt.babybuy.view.home.adapters.ProductRecyclerAdapter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShopFragment : Fragment(), ProductRecyclerAdapter.ProductAdapterListener {
    private lateinit var binding: FragmentMyItemsBinding
    private lateinit var productRecyclerAdapter: ProductRecyclerAdapter
    private lateinit var searchEditText: EditText

    private lateinit var startAddOrUpdateActivityForResult: ActivityResultLauncher<Intent>
    private lateinit var startDetailViewActivity: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAddOrUpdateActivityForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AddOrUpdateActivity.RESULT_CODE_COMPLETE) {
                setUpRecyclerView()
            } else {
                //TODO Do nothing
            }
        }

        startDetailViewActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == ProductDetailActivity.RESULT_CODE_REFRESH) {
                setUpRecyclerView()
            } else {
                //Do Nothing
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyItemsBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        setUpViews()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopFragment()
    }

    private fun setUpViews() {
        setUpFloatingActionButton()
        setUpRecyclerView()
        setUpSearchEditText()
    }

    private fun setUpRecyclerView() {
        val suitCaseDatabase = AppDatabase.getInstance(requireActivity().applicationContext)
        val productDao = suitCaseDatabase.productDao()

        Thread {
            try {
                val products = productDao.getAllProducts()
                if (products.isEmpty()) {
                    requireActivity().runOnUiThread {
                        ToastUtils.showToast(requireActivity(), "No Items Yet!")
                    }
                }
                requireActivity().runOnUiThread {
                    populateRecyclerView(products)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                requireActivity().runOnUiThread {
                    ToastUtils.showToast(requireActivity(), "Couldn't load items.")
                }
            }
        }.start()
    }

    private fun populateRecyclerView(products: List<Product>) {
        productRecyclerAdapter = ProductRecyclerAdapter(
            products,
            this,
            requireActivity().applicationContext
        )
        binding.rvShop.adapter = productRecyclerAdapter
        binding.rvShop.layoutManager = LinearLayoutManager(requireActivity())
    }


    private fun setUpFloatingActionButton() {
        binding.fabAddItem.setOnClickListener {
            val intent = Intent(requireActivity(), AddOrUpdateActivity::class.java)
            startAddOrUpdateActivityForResult.launch(intent)
        }
    }

    // Set up the search View
    private fun setUpSearchEditText() {
        searchEditText = binding.editTextSearch
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Set a TextWatcher to handle text changes
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Handle text changes here, e.g., perform search
                val query = s.toString()
                searchDatabase(query)
            }
        })



        // Set an OnFocusChangeListener to handle focus changes
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // When focused, show the clear icon
                searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_clear, 0)
            } else {
                // When not focused, remove the clear icon
                searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }


    // Search database
    private fun searchDatabase(query: String?) {
        val suitCaseDatabase = AppDatabase.getInstance(requireActivity().applicationContext)
        val productDao = suitCaseDatabase.productDao()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val products = productDao.searchDatabase("%$query%") // Use % to search for partial matches
                requireActivity().runOnUiThread {
                    populateRecyclerView(products)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                requireActivity().runOnUiThread {
                    ToastUtils.showToast(requireActivity(), "Error searching items.")
                }
            }
        }
    }

    // In case item list is empty
    private fun emptyView(){
        //Hide Login Button on loading
        binding.rvShop.visibility = View.GONE
        //Display Loading
        binding.linearLayoutEmpty.visibility = View.VISIBLE
    }

    // In case item list is not empty
    private fun notEmptyView(){
        //Hide Loading
     binding.linearLayoutEmpty.visibility = View.GONE
        //Display Login Button on loading
        binding.rvShop.visibility = View.VISIBLE
    }

    override fun onItemClicked(product: Product, position: Int) {
        val intent = Intent(requireActivity(), ProductDetailActivity::class.java)
        intent.putExtra(AppConstants.KEY_PRODUCT, product)
        intent.putExtra(AppConstants.KEY_PRODUCT_POSITION, position)
        startDetailViewActivity.launch(intent)
    }
}