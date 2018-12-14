package com.example.guka.firebaseapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*


class SignInActivity : AppCompatActivity() {

    // Checking if user is signed in
    val user = FirebaseAuth.getInstance().currentUser
    public override fun onStart() {
        super.onStart()
        if (user != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Animation function
    fun startFadeInAnimation(view: View) {
        val startAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in_animation)
        view.startAnimation(startAnimation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Click on SignIn Button
        signInButton.setOnClickListener{
            performLogin()
        }

        // Link to RegisterActivity
        dont_have_account_sign_up.setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Logo Animation
        startFadeInAnimation(signin_fireBaseLayout)
    }

    private fun performLogin() {
        val email = edit_text_signIn_email.text.toString()
        val password = edit_text_signIn_password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Authentication Successful", Toast.LENGTH_SHORT).show()
                    Log.d("Main", "Success", task.exception)
                    val user = auth.currentUser
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)

                }
            }.addOnFailureListener { it ->
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(baseContext, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}


