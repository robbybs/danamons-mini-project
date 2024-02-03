package com.rbs.danamontest.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import com.rbs.danamontest.databinding.ActivityMainBinding
import com.rbs.danamontest.ui.home.HomeActivity
import com.rbs.danamontest.ui.register.RegisterActivity
import com.rbs.danamontest.utils.ViewModelFactoryWithContext
import io.reactivex.Observable

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewmodel: MainViewModel by viewModels {
        ViewModelFactoryWithContext(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel.getUserSession().observe(this) {
            if (it == true) {
                viewmodel.getRole().observe(this) { role ->
                    goToHome(role)
                }
            } else {
                setDataStream()
                doLogin()
                goToSignup()
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
        val emailStream = RxTextView.textChanges(binding.inputEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.inputPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe {
            showPassword(it)
        }

        validateStream(emailStream, passwordStream)
    }

    private fun showEmailAlert(isNotValid: Boolean) {
        binding.inputEmail.error =
            if (isNotValid) "Email not valid" else null
    }

    private fun showPassword(isNotValid: Boolean) {
        binding.inputPassword.error =
            if (isNotValid) "Password is less than 6 character" else null
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
        binding.buttonLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            viewmodel.checkData(email).observe(this) {
                if (it != null) {
                    if (email == it.email && password == it.password) {
                        val role = it.role
                        viewmodel.saveSession(true)
                        if (role != null) viewmodel.saveRole(role)

                        startActivity(
                            Intent(
                                this,
                                HomeActivity::class.java
                            ).putExtra(HomeActivity.ROLE, role)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Wrong password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "No data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun goToSignup() {
        binding.buttonSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}