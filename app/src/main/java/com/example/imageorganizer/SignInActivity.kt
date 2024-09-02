package com.example.imageorganizer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imageorganizer.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var signinBinding : ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signinBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(signinBinding.root)


        signinBinding.textRegister.setOnClickListener {
            startActivity(Intent(applicationContext , SignUpActivity::class.java))
            finish()
        }



    }
}