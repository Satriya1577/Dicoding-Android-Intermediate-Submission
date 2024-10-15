package com.example.mystoryapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.databinding.ItemStoryBinding
import com.example.mystoryapp.ui.detail.DetailStoryActivity
import com.bumptech.glide.Glide


class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }
    class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ListStoryItem){
            Glide.with(binding.imgStory.context)
                .load(review.photoUrl)
                .into(binding.imgStory)
            binding.tvName.text = "${review.name}"
            binding.tvDeskripsi.text = "${review.description}"

            binding.root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, DetailStoryActivity::class.java)
                val story = ListStoryItem(photoUrl = review.photoUrl, name = review.name, description = review.description)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, story)
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}