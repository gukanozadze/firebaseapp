package com.example.guka.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.user_row_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class NewMessageActivity : AppCompatActivity() {

    val TAG = "FIRESTORETAG"
    companion object {
        val USER_KEY = "USER_KEY"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"


        fetchUsers()


    }

    private fun fetchUsers(){

        val users = FirebaseFirestore.getInstance().collection("users")

        val adapter = GroupAdapter<ViewHolder>()
        recyclerview_newmessage.adapter = adapter

        users
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adapter.add(UserItem(document.data["username"].toString(), document.data["uid"].toString()))
                    //new_message_username_text_view.text = document.data["username"].toString()
                }
            }
            .addOnFailureListener { exception ->
                toast("Failed to fetch Users")
            }

        adapter.setOnItemClickListener{item, view ->

            val userItem = item as UserItem

            val intent = Intent(view.context, ChatLogActivity::class.java)

            intent.putExtra("uid", item.uid)
            startActivity(intent)
            finish()
        }
    }
}

class UserItem(val user: String, val uid: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.new_message_username_text_view.text = user
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

}




