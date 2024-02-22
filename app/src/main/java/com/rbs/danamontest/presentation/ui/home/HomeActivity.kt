package com.rbs.danamontest.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rbs.danamontest.utils.GlobalSingleton
import com.rbs.danamontest.utils.GlobalSingletonListener
import com.rbs.danamontest.R
import com.rbs.danamontest.databinding.ActivityHomeBinding
import com.rbs.danamontest.presentation.ui.main.MainActivity
//import com.rbs.danamontest.utils.ViewModelFactoryWithContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var userAdapter: UserAdapter
//    private val viewmodel: HomeViewModel by viewModels {
//        ViewModelFactoryWithContext(applicationContext)
//    }
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
                viewmodel.getData().observe(this@HomeActivity) {
                    submitData(lifecycle, it)
                }

                addLoadStateListener {
                    if (it.append.endOfPaginationReached) {
                        if (itemCount < 1)
                            Toast.makeText(
                                this@HomeActivity,
                                getString(R.string.text_empty_data),
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }
            }
        } else {
            setUserAdapter()
            with(viewmodel) {
                getAllData().observe(this@HomeActivity) { data ->
                    userAdapter.submitList(data)

                    val itemTouchHelperCallback =
                        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                            override fun onMove(
                                recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder
                            ): Boolean = false

                            override fun onSwiped(
                                viewHolder: RecyclerView.ViewHolder,
                                direction: Int
                            ) {
                                val dataSwipePosition = data[viewHolder.bindingAdapterPosition].id
                                deleteData(dataSwipePosition)
                            }
                        }

                    val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                    itemTouchHelper.attachToRecyclerView(binding.rvData)
                }
            }
        }
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

    companion object {
        const val ROLE = "role"
    }

    private inner class Listener : GlobalSingletonListener {
        override fun onEvent() { }
    }
}