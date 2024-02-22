package com.rbs.danamontest.presentation.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.rbs.danamontest.utils.GlobalSingleton
import com.rbs.danamontest.utils.GlobalSingletonListener
import com.rbs.danamontest.R
import com.rbs.danamontest.databinding.ActivityMainBinding
import com.rbs.danamontest.presentation.ui.home.HomeActivity
import com.rbs.danamontest.presentation.ui.register.RegisterActivity
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewmodel: MainViewModel by viewModel()
    private val listener = Listener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupData()
    }

    override fun onStart() {
        super.onStart()
        GlobalSingleton.register(listener)
    }

    override fun onStop() {
        super.onStop()
        GlobalSingleton.unregister(listener)
    }

    private fun setupData() {
        with(viewmodel) {
            getUserSession().observe(this@MainActivity) {
                if (it == true) {
                    getRole().observe(this@MainActivity) { role ->
                        goToHome(role)
                    }
                } else {
                    setDataStream()
                    doLogin()
                    goToSignup()
                }
            }
        }
    }

    private fun goToHome(role: String) {
        startActivity(
            Intent(this, HomeActivity::class.java).putExtra(HomeActivity.ROLE, role)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }

    private fun setDataStream() {
        with(binding) {
            val emailStream = RxTextView.textChanges(inputEmail)
                .skipInitialValue()
                .map { email ->
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                }
            emailStream.subscribe {
                showEmailAlert(it)
            }

            val passwordStream = RxTextView.textChanges(inputPassword)
                .skipInitialValue()
                .map { password ->
                    password.length < 6
                }
            passwordStream.subscribe {
                showPassword(it)
            }

            validateStream(emailStream, passwordStream)
        }
    }

    private fun showEmailAlert(isNotValid: Boolean) {
        binding.inputEmail.error =
            if (isNotValid) getString(R.string.text_email_error) else null
    }

    private fun showPassword(isNotValid: Boolean) {
        binding.inputPassword.error =
            if (isNotValid) getString(R.string.text_password_error) else null
    }

    private fun validateStream(
        emailStream: Observable<Boolean>,
        passwordStream: Observable<Boolean>
    ) {
        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream
        ) { emailInvalid: Boolean, passwordInvalid: Boolean ->
            !emailInvalid && !passwordInvalid
        }

        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                with(binding.buttonLogin) {
                    isEnabled = true
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            android.R.color.black
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            android.R.color.white
                        )
                    )
                }
            } else {
                with(binding.buttonLogin) {
                    isEnabled = false
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            android.R.color.darker_gray
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            android.R.color.black
                        )
                    )
                }
            }
        }
    }

    private fun doLogin() {
        with(binding) {
            buttonLogin.setOnClickListener {
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()

                with(viewmodel) {
                    checkData(email).observe(this@MainActivity) {
                        if (it != null) {
                            if (email == it.email && password == it.password) {
                                val role = it.role
                                saveSession(true)
                                if (role != null) saveRole(role)

                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        HomeActivity::class.java
                                    ).putExtra(HomeActivity.ROLE, role)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                )
                                finish()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(R.string.text_wrong_password),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.text_empty_data),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun goToSignup() {
        binding.buttonSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private inner class Listener : GlobalSingletonListener {
        override fun onEvent() {}
    }
}