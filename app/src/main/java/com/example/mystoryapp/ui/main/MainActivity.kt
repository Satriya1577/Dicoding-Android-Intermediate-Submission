package com.example.mystoryapp.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.StoryAdapter
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.ViewModelFactory
import com.example.mystoryapp.ui.maps.MapsActivity
import com.example.mystoryapp.ui.upload.UploadStoryActivity
import com.example.mystoryapp.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                setupView()
                setupAction()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setSupportActionBar(binding.toolbar)
    }


    private fun setupAction() {
        val storyAdapter = StoryAdapter()
        viewModel.getStories().observe(this) { result ->
            if (result != null) {
                when(result) {
                    is Result.Success -> {
                        val items = ArrayList<ListStoryItem>()
                        result.data.forEach {
                            items.add(it)
                        }
                        if (items.isEmpty()) {
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            storyAdapter.submitList(items)
                            binding.rvStories.apply {
                                layoutManager = LinearLayoutManager(this@MainActivity)
                                adapter = storyAdapter
                            }
                        }
                        binding.tvNoData.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                    }
                    is Result.Error -> {
                        AlertDialog.Builder(this).apply {
                            setTitle(R.string.failed_title)
                            setMessage(result.error)
                            setPositiveButton(R.string.positive_reply) { dialog, _ ->
                                dialog.dismiss()
                            }
                            create()
                            show()
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.btnAddStories.setOnClickListener {
            val moveIntent = Intent(this@MainActivity, UploadStoryActivity::class.java)
            startActivity(moveIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.action_logout -> {
                viewModel.logout()
            }
            R.id.action_map -> {
                val moveIntent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStories()
    }
}