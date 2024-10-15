package com.example.mystoryapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
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
                val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
                val story = ListStoryItem(photoUrl = review.photoUrl, name = review.name, description = review.description)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        binding.root.context as Activity,
                        Pair(binding.imgStory, "profile"),
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDeskripsi, "description"),
                    )
                binding.root.context.startActivity(intent,optionsCompat.toBundle())
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