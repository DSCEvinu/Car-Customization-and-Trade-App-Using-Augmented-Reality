package com.example.carease.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.carease.R
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fullName: EditText
    private lateinit var emailEt: EditText
    private lateinit var passEt: EditText
    private lateinit var CpassEt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth
        val signUpBtn = findViewById<Button>(R.id.signUpBtn_signUpPage)
        fullName = findViewById(R.id.nameEt_signUpPage)
        emailEt = findViewById(R.id.emailEt_signUpPage)
        passEt = findViewById(R.id.PassEt_signUpPage)
        CpassEt = findViewById(R.id.cPassEt_signUpPage)
        val signInTv = findViewById<TextView>(R.id.signInTv_signUpPage)

        signInTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signUpBtn.setOnClickListener {
            performSignUp()
        }

    }

    private fun performSignUp() {
        val email = emailEt.text.toString().trim()
        val password = passEt.text.toString().trim()
        val confirmPassword = CpassEt.text.toString().trim()
        val name = fullName.text.toString().trim()

        // Validation checks
        when {
            name.isEmpty() -> {
                fullName.error = "Name is required"
                return
            }

            email.isEmpty() -> {
                emailEt.error = "Email is required"
                return
            }

            password.isEmpty() -> {
                passEt.error = "Password is required"
                return
            }

            confirmPassword.isEmpty() -> {
                CpassEt.error = "Confirm password is required"
                return
            }

            password != confirmPassword -> {
                CpassEt.error = "Passwords do not match"
                return
            }

            password.length < 6 -> {
                passEt.error = "Password must be at least 6 characters"
                return
            }
        }

        // Firebase signup
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this, "Sign up failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}