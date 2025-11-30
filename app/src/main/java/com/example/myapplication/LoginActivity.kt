package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {

    lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DBHelper(this)

        // MATCH XML IDs
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val loginBtn = findViewById<Button>(R.id.btnLogin)
        val signUpText = findViewById<TextView>(R.id.tvSignUp)


        // LOGIN BUTTON
        loginBtn.setOnClickListener {
            val user = db.login(email.text.toString(), password.text.toString())

            if (user == null) {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            } else {
                val i = Intent(this, TableActivity::class.java)
                i.putExtra("role", user.role)
                startActivity(i)
            }
        }

        // SIGN UP TEXT
        signUpText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
