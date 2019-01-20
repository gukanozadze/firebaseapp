package com.example.guka.firebaseapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.time.LocalDateTime
import java.util.*


class SignInActivity : AppCompatActivity() {

    // Checking if user is signed in
    val user = FirebaseAuth.getInstance().currentUser
    public override fun onStart() {
        super.onStart()
        if (user != null) {
            startActivity<LatestMessagesActivity>()
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

            // Hide Keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            performLogin()
        }

        // Link to RegisterActivity
        dont_have_account_sign_up.setOnClickListener{
            startActivity<RegisterActivity>()
        }

        // Logo Animation
        startFadeInAnimation(signin_fireBaseLayout)
    }

    private fun performLogin() {
        val email = edit_text_signIn_email.text.toString()
        val password = edit_text_signIn_password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            toast("Please Fill In All Fields")
            return
        }

        //  Anko Progress bar
        val progressDialog = indeterminateProgressDialog("Logging In")

        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Stop ProgressBar + Link to Main Activity
                    progressDialog.dismiss()
                    startActivity<MainActivity>()
                }
            }.addOnFailureListener { it ->
                progressDialog.dismiss()
                toast("${it.message}")
                Log.d("LoginUser", "Failed to create user: ${it.message}")
            }
    }
}


