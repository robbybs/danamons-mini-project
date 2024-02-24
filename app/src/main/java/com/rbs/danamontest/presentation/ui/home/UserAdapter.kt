package com.rbs.danamontest.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.databinding.ItemUserBinding

class UserAdapter : ListAdapter<UserEntity, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    inner class MyViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserEntity) {
            with(binding) {
                val userId = data.id
                val username = data.username

                tvUsername.text = username
                tvEmail.text = data.email
                tvRole.text = data.role
                tvIds.text = userId.toString()

                buttonDelete.setOnClickListener {
                    if (username != null) onItemClickCallback?.onUpdateItem(userId)
                }

                buttonUpdate.setOnClickListener {
                    onItemClickCallback?.onDeleteItem(userId)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onUpdateItem(id: Int)

        fun onDeleteItem(id: Int)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}