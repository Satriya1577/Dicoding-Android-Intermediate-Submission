package com.example.mystoryapp.ui.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
            }
        }

        setupView()
        setupAction()
        // playAnimation()
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
                        storyAdapter.submitList(items)
                        binding.rvStories.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = storyAdapter
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                    is Result.Error -> {
                        AlertDialog.Builder(this).apply {
                            setTitle("Warning!")
                            setMessage("Terjadi kesalahan saat mengambil data story ${result.error} ")
                            setPositiveButton("Lanjut") { dialog, _ ->
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting_language -> {

            }
            R.id.action_logut -> {
               viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 6000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()
//
//        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
//        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
//        val logout = ObjectAnimator.ofFloat(binding.logoutButton, View.ALPHA, 1f).setDuration(100)
//
//        AnimatorSet().apply {
//            playSequentially(name, message, logout)
//            startDelay = 100
//        }.start()
//    }
}