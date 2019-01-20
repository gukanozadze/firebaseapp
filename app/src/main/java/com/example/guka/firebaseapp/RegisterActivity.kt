package com.example.guka.firebaseapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*


class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Register Button
        registerButton.setOnClickListener {

            // Hide Keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

            performRegister()
        }

        // Link to SignInActivity
        already_have_account_sign_in.setOnClickListener{
            startActivity<SignInActivity>()
        }
    }

    private fun performRegister(){
        val email = edit_text_register_email.text.toString()
        val password = edit_text_register_password.text.toString()
        val username = edit_text_register_username.text.toString()

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()){
            toast("Please fill in all the fields")
            return
        }

        val progressDialog = indeterminateProgressDialog("Registering")
        // Firebase authentication to create user with email and password
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    saveUserToDatabase(username, email)

                    // Success Message + Redirecting
                    progressDialog.dismiss()
                    startActivity<RegisterActivity>()
                }
            }.addOnFailureListener{it ->
                progressDialog.dismiss()
                Log.d("CreateUser", "Failed to create user: ${it.message}")
                toast("${it.message}")
            }
    }

    private fun saveUserToDatabase(username: String, email: String){

        // Firestore DB
        val db = FirebaseFirestore.getInstance()
        // Uid of Current User
        val uid = FirebaseAuth.getInstance().uid ?: ""

        // Creating Dictionary of user
        val user = HashMap<String, Any>()
        user["uid"] = uid
        user["username"] = username.capitalize()
        user["date"] = Date()
        user["email"] = email

        // Saving To database
        val users = db.collection("users")
        users.document(uid).set(user)

    }
}
