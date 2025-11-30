package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class TableActivity : AppCompatActivity() {

    lateinit var db: DBHelper
    lateinit var listView: ListView
    lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        db = DBHelper(this)
        listView = findViewById(R.id.listUsers)
        btnLogout = findViewById(R.id.btnLogout)

        val role = intent.getStringExtra("role") ?: "user"

        // Load users based on role
        loadUsers(role)

        // Logout function
        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Prevent going back
    }

    private fun loadUsers(role: String) {
        val users = db.getUsers()
        val listItems = users.map { "${it.username} | ${it.role}" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter

        // Only admin can edit/delete
        if (role.lowercase() == "admin") {
            listView.setOnItemClickListener { _, _, position, _ ->
                showEditDeleteDialog(users[position])
            }
        } else {
            // Disable click for non-admin users
            listView.setOnItemClickListener { _, _, _, _ ->
                Toast.makeText(this, "Only admin can edit users", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditDeleteDialog(user: User) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_edit_user, null)

        val etUser = dialogView.findViewById<EditText>(R.id.etEditUser)
        val etPass = dialogView.findViewById<EditText>(R.id.etEditPass)
        val etRole = dialogView.findViewById<EditText>(R.id.etEditRole)

        etUser.setText(user.username)
        etPass.setText(user.password)
        etRole.setText(user.role)

        AlertDialog.Builder(this)
            .setTitle("Edit / Delete User")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                db.updateUser(
                    user.id,
                    etUser.text.toString(),
                    etPass.text.toString(),
                    etRole.text.toString()
                )
                loadUsers("admin")
            }
            .setNegativeButton("Delete") { _, _ ->
                db.deleteUser(user.id)
                loadUsers("admin")
            }
            .setNeutralButton("Cancel", null)
            .show()
    }
}
