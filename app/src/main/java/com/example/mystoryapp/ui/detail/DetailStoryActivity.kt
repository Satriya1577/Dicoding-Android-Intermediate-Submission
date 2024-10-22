package com.example.mystoryapp.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding
import com.example.mystoryapp.ui.main.MainActivity

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView () {
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupAction() {
        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_STORY)
        }

        val imgStory = findViewById<ImageView>(R.id.iv_detail_photo)
        val tvName = findViewById<TextView>(R.id.tv_detail_name)
        val tvDescription = findViewById<TextView>(R.id.tv_detail_description)

        Glide.with(this)
            .load(story?.photoUrl)
            .into(imgStory)
        tvName.text = story?.name
        tvDescription.text = story?.description
        supportActionBar?.title = story?.name

        binding.btnBack.setOnClickListener {
            val moveIntent = Intent(this@DetailStoryActivity, MainActivity::class.java)
            startActivity(moveIntent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object  {
        const val EXTRA_STORY = "extra_story"
    }
}