package com.example.mystoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.data.pref.UserModel
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.ui.ViewModelFactory
import com.example.mystoryapp.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
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
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when(result) {
                        is Result.Success -> {
                            viewModel.saveSession(UserModel(
                                userId = "${result.data.loginResult?.userId}",
                                token = "${result.data.loginResult?.token}",
                                name = "${result.data.loginResult?.name}",
                                isLogin = true
                            ))
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.success_title)
                                setMessage(R.string.success_login_message)
                                setPositiveButton(R.string.positive_reply) { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
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
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}