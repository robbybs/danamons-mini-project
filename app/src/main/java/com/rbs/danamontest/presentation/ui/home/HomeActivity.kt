package com.rbs.danamontest.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rbs.danamontest.R
import com.rbs.danamontest.databinding.ActivityHomeBinding
import com.rbs.danamontest.presentation.ui.main.MainActivity
import com.rbs.danamontest.utils.GlobalSingleton
import com.rbs.danamontest.utils.GlobalSingletonListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var userAdapter: UserAdapter
    private val viewmodel: HomeViewModel by viewModel()
    private val listener = Listener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        setLogout()
    }

    override fun onStart() {
        super.onStart()
        GlobalSingleton.register(listener)
    }

    override fun onStop() {
        super.onStop()
        GlobalSingleton.unregister(listener)
    }

    private fun getData() {
        val role = intent.getStringExtra(ROLE).toString()

        if (role != "Admin") {
            setHomeAdapter()
            with(homeAdapter) {
                viewmodel.getDataFromApi.observe(this@HomeActivity) {
                    submitData(lifecycle, it)
                }

                addLoadStateListener {
                    if (it.append.endOfPaginationReached) {
                        if (itemCount < 1) setToastMessage(getString(R.string.text_empty_data))
                    }
                }
            }
        } else {
            setUserAdapter()
            with(viewmodel) {
                getUserData.observe(this@HomeActivity) { data ->
                    if (data.isEmpty() || data == null) {
                        binding.tvNoData.visibility = View.VISIBLE
                    } else {
                        with(userAdapter) {
                            submitList(data)
                            setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                                override fun onUpdateItem(id: Int) {
                                    val title = getString(R.string.text_update)
                                    val layout = R.layout.item_update
                                    val inputLayout = R.id.input_username_confirm
                                    val message = getString(R.string.text_username_error)
                                    setDialog(title, id, layout, inputLayout, message)
                                }

                                override fun onDeleteItem(id: Int) {
                                    val title = getString(R.string.text_delete)
                                    val layout = R.layout.item_delete
                                    val inputLayout = R.id.input_password_confirm
                                    val message = getString(R.string.text_password_error)
                                    setDialog(title, id, layout, inputLayout, message)
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    private fun setToastMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setHomeAdapter() {
        homeAdapter = HomeAdapter()
        with(binding.rvData) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = homeAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter()
            )
        }
    }

    private fun setUserAdapter() {
        userAdapter = UserAdapter()
        with(binding.rvData) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = userAdapter
        }
    }

    private fun setLogout() {
        binding.buttonLogout.setOnClickListener {
            viewmodel.saveSession(false)
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
            finishAffinity()
        }
    }

    @SuppressLint("InflateParams")
    private fun setDialog(title: String, id: Int, layout: Int, inputLayout: Int, message: String) {
        val dialog = MaterialAlertDialogBuilder(this)
        val context = dialog.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layout, null, false)
        val isUpdate = title == getString(R.string.text_update)
        val isDelete = title == getString(R.string.text_delete)

        with(dialog) {
            setTitle("$title Data")
            if (isDelete) setMessage(getString(R.string.text_confirmation))
            setView(view)
            setNegativeButton(getString(R.string.text_cancel)) { dialog, _ ->
                // Respond to negative button press
                dialog.cancel()
            }
            setPositiveButton(title) { _, _ ->
                // Respond to positive button press
                val inputView = view.findViewById<AppCompatEditText>(inputLayout)
                val inputValue = inputView.text.toString()

                if (inputValue.isEmpty() || inputView.text!!.length < 6) {
                    setToastMessage(message)
                } else {
                    if (isUpdate) {
                        viewmodel.updateData(id, inputValue)
                        refresh()
                    } else {
                        with(viewmodel) {
                            getUserPassword.observe(this@HomeActivity) {
                                if (inputValue == it) {
                                    deleteData(id)
                                    refresh()
                                } else {
                                    setToastMessage(getString(R.string.text_wrong_password))
                                }
                            }
                        }
                    }
                }
            }
                .show()
        }
    }

    private fun refresh() {
        finish()
        startActivity(intent)
    }

    companion object {
        const val ROLE = "role"
    }

    private inner class Listener : GlobalSingletonListener {
        override fun onEvent() {}
    }
}