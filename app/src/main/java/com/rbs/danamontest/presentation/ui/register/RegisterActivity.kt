package com.rbs.danamontest.presentation.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.rbs.danamontest.R
import com.rbs.danamontest.data.local.entity.UserEntity
import com.rbs.danamontest.databinding.ActivityRegisterBinding
import com.rbs.danamontest.presentation.ui.main.MainActivity
import com.rbs.danamontest.utils.GlobalSingleton
import com.rbs.danamontest.utils.GlobalSingletonListener
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var roleValue: String
    private val viewmodel: RegisterViewModel by viewModel()
    private val listener = Listener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDataStream()
        doSignup()
        goToLogin()
    }

    override fun onStart() {
        super.onStart()
        GlobalSingleton.register(listener)
    }

    override fun onStop() {
        super.onStop()
        GlobalSingleton.unregister(listener)
    }

    private fun setDataStream() {
        with(binding) {
            val usernameStream = RxTextView.textChanges(inputUsername)
                .skipInitialValue()
                .map { username ->
                    username.length < 6
                }
            usernameStream.subscribe {
                showAlert(it, inputUsername)
            }

            val emailStream = RxTextView.textChanges(inputEmail)
                .skipInitialValue()
                .map { email ->
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                }
            emailStream.subscribe {
                showAlert(it, inputEmail)
            }

            val passwordStream = RxTextView.textChanges(inputPassword)
                .skipInitialValue()
                .map { password ->
                    password.length < 6
                }
            passwordStream.subscribe {
                showAlert(it, inputPassword)
            }

            val roleStream = RxRadioGroup.checkedChanges(groupRole)
                .skipInitialValue()
                .map { radio ->
                    radio < 2
                }

            validateStream(usernameStream, emailStream, passwordStream, roleStream)
        }
    }

    private fun showAlert(isNotValid: Boolean, inputField: AppCompatEditText) {
        with(binding) {
            when (inputField) {
                inputUsername -> inputUsername.error =
                    if (isNotValid) getString(R.string.text_username_error) else null

                inputEmail -> inputEmail.error =
                    if (isNotValid) getString(R.string.text_email_error) else null

                else -> inputPassword.error =
                    if (isNotValid) getString(R.string.text_password_error) else null
            }
        }
    }

    private fun validateStream(
        usernameStream: Observable<Boolean>,
        emailStream: Observable<Boolean>,
        passwordStream: Observable<Boolean>,
        roleStream: Observable<Boolean>
    ) {
        val invalidFieldsStream = Observable.combineLatest(
            usernameStream,
            emailStream,
            passwordStream,
            roleStream
        ) { usernameInvalid: Boolean, emailInvalid: Boolean, passwordInvalid: Boolean, roleInvalid: Boolean ->
            !usernameInvalid && !emailInvalid && !passwordInvalid && !roleInvalid
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
        with(binding.buttonSignup) {
            isEnabled = state
            backgroundTintList = backgroundColor
            setTextColor(
                ContextCompat.getColor(
                    this@RegisterActivity,
                    textColor
                )
            )
        }
    }

    private fun doSignup() {
        with(binding) {
            buttonSignup.setOnClickListener {
                val username = inputUsername.text?.trim().toString()
                val email = inputEmail.text?.trim().toString()
                val password = inputPassword.text?.trim().toString()
                val roleAdmin = admin.text.toString()
                val roleUser = user.text.toString()

                roleValue = if (admin.isChecked) roleAdmin else roleUser

                val insertToDatabase =
                    UserEntity(
                        username = username,
                        email = email,
                        password = password,
                        role = roleValue
                    )
                viewmodel.insert(insertToDatabase)
                setToastMessage(getString(R.string.text_register_success))

                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finishAffinity()
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

    private fun goToLogin() {
        binding.buttonLogin.setOnClickListener {
            startActivity(
                Intent(
                    this@RegisterActivity,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
    }

    private inner class Listener : GlobalSingletonListener {
        override fun onEvent() {}
    }
}