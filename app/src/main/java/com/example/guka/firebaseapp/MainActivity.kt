package com.example.guka.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.guka.firebaseapp.R.id.navigation_people
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Getting Current User
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {

            // Setting Email of user in TextView
            val userEmail = user.email.toString()
            textViewEmail.text = userEmail
        }



        logout.setOnClickListener{
            logout()
        }
    }


    // Logout Handler
    fun logout(){
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(applicationContext, SignInActivity::class.java)
        startActivity(intent)
    }
}
