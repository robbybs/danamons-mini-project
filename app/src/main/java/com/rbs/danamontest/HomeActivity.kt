package com.rbs.danamontest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rbs.danamontest.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var userAdapter: UserAdapter
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactoryWithContext(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        setLogout()
    }

    private fun getData() {
        val role = intent.getStringExtra(ROLE).toString()

        if (role != "Admin") {
            setHomeAdapter()
            viewModel.getData().observe(this) {
                homeAdapter.submitData(lifecycle, it)
            }

            homeAdapter.addLoadStateListener {
                if (it.append.endOfPaginationReached) {
                    if (homeAdapter.itemCount < 1)
                        Toast.makeText(
                            this,
                            "No data",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        } else {
            setUserAdapter()

            viewModel.getAllData().observe(this) { data ->
                userAdapter.submitList(data)

                val itemTouchHelperCallback =
                    object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean = false

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val dataSwipePosition = data[viewHolder.bindingAdapterPosition].id
                            dataSwipePosition.let {
                                viewModel.deleteData(it)
                            }
                        }
                    }

                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                itemTouchHelper.attachToRecyclerView(binding.rvData)
            }
        }
    }

    private fun setHomeAdapter() {
        homeAdapter = HomeAdapter()
        with(binding) {
            rvData.layoutManager = LinearLayoutManager(this@HomeActivity)
            rvData.adapter = homeAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter()
            )
        }

    }

    private fun setUserAdapter() {
        userAdapter = UserAdapter()
        with(binding) {
            rvData.layoutManager = LinearLayoutManager(this@HomeActivity)
            rvData.adapter = userAdapter
        }
    }

    private fun setLogout() {
        binding.buttonLogout.setOnClickListener {
            viewModel.saveSession(false)
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
}