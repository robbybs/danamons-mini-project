package com.rbs.danamontest.presentation.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
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
            getSession.observe(this@MainActivity) {
                if (it == true) {
                    getUserRole.observe(this@MainActivity) { role ->
                        goToHome(role)
                    }
                } else {
                    showLayout()
                    setDataStream()
                    doLogin()
                    goToSignup()
                }
            }
        }
    }

    private fun showLayout() {
        binding.container.visibility = View.VISIBLE
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (isValid) {
                    val state = true
                    val backgroundColor = getColorStateList(R.color.alt_dark)
                    val textColor = R.color.alt_light
                    setButton(state, backgroundColor, textColor)
                } else {
                    val state = false
                    val backgroundColor = getColorStateList(R.color.alt_light)
                    val textColor = R.color.alt_dark
                    setButton(state, backgroundColor, textColor)
                }
            }
        }
    }

    private fun setButton(state: Boolean, backgroundColor: ColorStateList, textColor: Int) {
        with(binding.buttonLogin) {
            isEnabled = state
            backgroundTintList = backgroundColor
            setTextColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    textColor
                )
            )
        }
    }

    private fun doLogin() {
        with(binding) {
            buttonLogin.setOnClickListener {
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()

                with(viewmodel) {
                    checkUser(email, password).observe(this@MainActivity) {
                        if (it != null) {
                            if (email == it.email && password == it.password) {
                                val role = it.role
                                val userPassword = it.password

                                if (userPassword != null) savePassword(userPassword)
                                if (role != null) saveRole(role)

                                saveSession(true)
                                setToastMessage(getString(R.string.text_login_success))

                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        HomeActivity::class.java
                                    ).putExtra(HomeActivity.ROLE, role)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                )
                            } else {
                                setToastMessage(getString(R.string.text_wrong_password))
                            }
                        } else {
                            setToastMessage(getString(R.string.text_empty_data))
                        }
                    }
                }
            }
        }
    }

    private fun setToastMessage(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
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