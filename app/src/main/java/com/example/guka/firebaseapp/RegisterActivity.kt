package com.example.guka.firebaseapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Register Button
        registerButton.setOnClickListener {
            performRegister()
        }

        // Link to SignInActivity
        already_have_account_sign_in.setOnClickListener{
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister(){
        val email = edit_text_register_email.text.toString()
        val password = edit_text_register_password.text.toString()
        val username = edit_text_register_username.text.toString()

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()){
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase authentication to create user with email and password
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    saveUserToDatabase(username)
                }
            }.addOnFailureListener{it ->
                Log.d("CreateUser", "Failed to create user: ${it.message}")
                Toast.makeText(baseContext, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToDatabase(username: String){

        // Firestore DB
        val db = FirebaseFirestore.getInstance()
        // Uid of Current User
        val uid = FirebaseAuth.getInstance().uid ?: ""


        // Creating Dictionary of user
        val user = HashMap<String, Any>()
        user["uid"] = uid
        user["username"] = username

        // Saving To database
        val users = db.collection("users")
        users.document(uid).set(user)

        // Success Message + Redirecting
        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }
}
