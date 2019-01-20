package com.example.guka.firebaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity() {

    private val TAG = "GUKATAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        val users = db.collection("users")

        users
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, document.id + " => " + document.data["username"])
                    username_text_view.text = document.data["username"].toString()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}
