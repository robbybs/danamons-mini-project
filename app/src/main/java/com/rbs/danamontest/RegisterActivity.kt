package com.rbs.danamontest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.rbs.danamontest.databinding.ActivityRegisterBinding
import io.reactivex.Observable

@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var roleValue: String
    private val viewmodel: RegisterViewModel by viewModels {
        ViewModelFactoryWithContext(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDataStream()
        doSignup()
        goToLogin()
    }

    private fun setDataStream() {
        val usernameStream = RxTextView.textChanges(binding.inputUsername)
            .skipInitialValue()
            .map { username ->
                username.length < 6
            }
        usernameStream.subscribe {
            showUsernameAlert(it)
        }

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

        val roleStream =
            RxRadioGroup.checkedChanges(binding.groupRole).skipInitialValue().map { radio ->
                radio < 2
            }

        validateStream(usernameStream, emailStream, passwordStream, roleStream)
    }

    private fun showUsernameAlert(isNotValid: Boolean) {
        binding.inputUsername.error =
            if (isNotValid) "Username is less than 6 character" else null
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
            if (isValid) {
                with(binding.buttonSignup) {
                    isEnabled = true
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@RegisterActivity,
                            android.R.color.black
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this@RegisterActivity,
                            android.R.color.white
                        )
                    )
                }
            } else {
                with(binding.buttonSignup) {
                    isEnabled = false
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@RegisterActivity,
                            android.R.color.darker_gray
                        )
                    )
                    setTextColor(
                        ContextCompat.getColor(
                            this@RegisterActivity,
                            android.R.color.black
                        )
                    )
                }
            }
        }
    }

    private fun doSignup() {
        with(binding) {
            buttonSignup.setOnClickListener {
                val username = inputUsername.text.toString()
                val email = inputEmail.text.toString()
                val password = inputPassword.text.toString()
                val roleAdmin = admin.text.toString()
                val roleUser = user.text.toString()

                roleValue = if (admin.isChecked) roleAdmin else roleUser

                val insertToDatabase =
                    User(username = username, email = email, password = password, role = roleValue)
                viewmodel.insert(insertToDatabase)

                Toast.makeText(this@RegisterActivity, "Register success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finishAffinity()
            }
        }
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
}