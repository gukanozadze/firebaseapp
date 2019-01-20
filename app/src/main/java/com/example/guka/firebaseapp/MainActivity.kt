package com.example.guka.firebaseapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val TAG = "GUKATAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Getting Current User and Firebase Reference
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        if (user != null) {

            val uid = user.uid
            val email = user.email
            // Create a reference to Firestore
            val reference = db.collection("users").document(uid)

            reference.get()
                .addOnSuccessListener { document ->
                    val username = document["username"].toString()
                    textViewEmail.text = username + "\n"+ email
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }

        }





        // Users Button
        button_users.setOnClickListener{
            startActivity<NewMessageActivity>()
        }

        // Logging Out
        button_logout.setOnClickListener{

            FirebaseAuth.getInstance().signOut()
            startActivity<SignInActivity>()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.menu_new_message  -> {

                startActivity<NewMessageActivity>()
            }
            R.id.menu_sign_out -> {

                // Sign out
                FirebaseAuth.getInstance().signOut()
                startActivity<SignInActivity>()
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
