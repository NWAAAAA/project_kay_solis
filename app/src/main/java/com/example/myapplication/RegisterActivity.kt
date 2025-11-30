package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = DBHelper(this)

        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val spinnerRole = findViewById<Spinner>(R.id.spinnerRole)
        val signUpBtn = findViewById<Button>(R.id.btnSignUp)
        val loginText = findViewById<TextView>(R.id.tvLogin)
        val backBtn = findViewById<ImageView>(R.id.btnBack)

        // Spinner role options (guest → user)
        val roles = arrayOf("user", "admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        signUpBtn.setOnClickListener {
            val selectedRole = spinnerRole.selectedItem.toString()

            val inserted = db.insertUser(
                username.text.toString(),
                password.text.toString(),
                selectedRole
            )

            if (inserted)
                Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Failed to register!", Toast.LENGTH_SHORT).show()
        }

        loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        backBtn.setOnClickListener {
            finish()
        }
    }
}
