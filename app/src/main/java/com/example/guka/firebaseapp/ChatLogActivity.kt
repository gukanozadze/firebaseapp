package com.example.guka.firebaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import org.jetbrains.anko.toast

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat"
        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ChatFromItem("FROM MESSAGE ...."))
        adapter.add(ChatToItem("To Mesasge lmao go away fuck u"))

        recyclerview_chat_log.adapter = adapter


        send_edittext_button.setOnClickListener{
            performSendMessage()
        }
    }

    class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String, val timeStamp: Long){

    }
    private fun performSendMessage(){
        val text = send_edittext_button.text.toString()

        val db = FirebaseFirestore.getInstance().collection("messages")

        val data = HashMap<String, Any>()
        data["id"] = db.document().id
        data["fromId"] = FirebaseAuth.getInstance().uid.toString()
        data["toId"] = intent.getStringExtra("uid")
        data["timestamp"] = System.currentTimeMillis() / 1000
        data["text"] = text

        db.add(data)
            .addOnSuccessListener { documentReference ->
                toast("Sent")
            }
            .addOnFailureListener { e ->
                toast("Not Sent")
            }
    }
}


class ChatFromItem(val text: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = text
    }
}

class ChatToItem(val text: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text

    }
}
