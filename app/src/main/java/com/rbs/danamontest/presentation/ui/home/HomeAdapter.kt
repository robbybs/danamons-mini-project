package com.rbs.danamontest.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rbs.danamontest.R
import com.rbs.danamontest.data.remote.response.PhotoResponse
import com.rbs.danamontest.databinding.ItemHomeBinding

class HomeAdapter : PagingDataAdapter<PhotoResponse, HomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    inner class MyViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PhotoResponse) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.thumbnailUrl)
                    .apply(RequestOptions().override(70, 70))
                    .into(avatar)

                if (data.title.isEmpty()) tvTitle.text =
                    itemView.context.getString(R.string.text_unknown) else tvTitle.text = data.title

                tvUrl.text = data.url
                tvId.text = data.id.toString()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhotoResponse>() {
            override fun areItemsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PhotoResponse, newItem: PhotoResponse): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}