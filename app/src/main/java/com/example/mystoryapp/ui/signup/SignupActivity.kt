package com.example.mystoryapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mystoryapp.data.remote.Result
import com.example.mystoryapp.databinding.ActivitySignUpBinding
import com.example.mystoryapp.ui.ViewModelFactory
import com.example.mystoryapp.ui.main.MainViewModel
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
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
        binding.signupButton.setOnClickListener {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val email = binding.emailEditText.text.toString()

            if (binding.passwordEditText.text.toString().isEmpty() || binding.emailEditText.text.toString().isEmpty() || binding.nameEditText.text.toString().isEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("Warning!")
                    setMessage("Datanya harus diisi semua yah adick maniez")
                    setPositiveButton("Oke kak") { dialog,_ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            } else if (binding.passwordEditText.text.toString().length < 8) {
                AlertDialog.Builder(this).apply {
                    setTitle("Warning!")
                    setMessage("Passwordnya diubah lagi yuk jadi minimal 8 karakter ajaa")
                    setPositiveButton("Siap") { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            } else if (!email.matches(emailPattern.toRegex())) {
                AlertDialog.Builder(this).apply {
                    setTitle("Warning!")
                    setMessage("Email yang dimasukin ga valid, yok diubah lagi")
                    setPositiveButton("Siap") { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            } else  {
                val name = binding.nameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                viewModel.register(name, email, password).observe(this) { result ->
                    if (result != null) {
                        when(result) {
                            is Result.Success -> {
                                AlertDialog.Builder(this).apply {
                                    setTitle("Yeah!")
                                    setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                                    setPositiveButton("Lanjut") { _, _ ->
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                                binding.progressBar.visibility = View.GONE
                            }
                            is Result.Error -> {
                                AlertDialog.Builder(this).apply {
                                    setTitle("Warning!")
                                    setMessage("Akun dengan $email ${result.error}")
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
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}