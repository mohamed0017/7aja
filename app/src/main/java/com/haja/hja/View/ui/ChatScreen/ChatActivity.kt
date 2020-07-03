package com.haja.hja.View.ui.ChatScreen

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.haja.hja.OnItemClickWithId
import com.haja.hja.R
import com.haja.hja.Service.model.ChatMessagesDataModel
import com.haja.hja.Utils.ApplicationLanguageHelper
import com.haja.hja.Utils.LANG
import com.haja.hja.Utils.SharedPreferenceUtil
import com.haja.hja.Utils.USERID
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.delete_message.*
import java.util.*

class ChatActivity : AppCompatActivity(), OnItemClickWithId {

    private lateinit var viewModel: ChatViewModel
    private var adapter: ChatMessagesAdapter? = null
    private val mInterval = 30 * 1000 // 5 seconds by default, can be changed later
    private var mHandler: Handler? = null
    private var mStatusChecker: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        mHandler = Handler()

        getLastMessages()
        sendMessage()
        checkForNewMessages()
        chatBarBack.setOnClickListener {
            onBackPressed()
        }
        val userName = intent?.extras?.getString("user2_name")
        chatBarTitle.text = "${resources.getString(R.string.chatWith)} $userName"
    }

    private fun checkForNewMessages() {
        mStatusChecker = object : Runnable {
            override fun run() {
                try {
                    getMessages() //this function can change value of mInterval.
                } finally {
                    // 100% guarantee that this always happens, even if
                    // your update method throws an exception
                    mHandler?.postDelayed(this, mInterval.toLong())
                }
            }
        }
        mStatusChecker?.run()
    }

    private fun getLastMessages() {
        adapter = ChatMessagesAdapter(this, this)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = adapter

        getMessages()
    }

    private fun getMessages() {
        val userId = intent?.extras?.getString("user2_id")
        viewModel.getUserChat(userId!!.toInt()).observe(this, Observer { messages ->
            chatProgressBar.visibility = View.GONE
            if (messages != null) {
                if (messages.messageData != null) {
                    adapter?.setMessagesList(messages.messageData as ArrayList<ChatMessagesDataModel>)
                    adapter?.notifyDataSetChanged()
                }
            } else
                makeToast(this, resources.getString(R.string.error))
        })
    }

    private fun sendMessage() {
        val user2Id = intent?.extras?.getString("user2_id")
        val userId = SharedPreferenceUtil(this).getString(USERID, "0")

        sendButton.setOnClickListener {
            if (isValidMessage()) {
                sendProgressBar.visibility = View.VISIBLE
                sendButton.visibility = View.GONE
                viewModel.addMessage(getMessage()).observe(this, Observer { result ->
                    sendProgressBar.visibility = View.GONE
                    sendButton.visibility = View.VISIBLE

                    if (result != null) {
                        val newMessage = ChatMessagesDataModel()
                        newMessage.id = result.insertId
                        newMessage.message = messageEditText.text.toString()
                        newMessage.userId2 = user2Id
                        newMessage.userId = userId
                        adapter?.addItemToMessages(newMessage)
                        adapter?.notifyDataSetChanged()
                        messageRecyclerView.scrollToPosition(adapter!!.itemCount - 1)
                        messageEditText.setText("")
                    } else
                        makeToast(this, resources.getString(R.string.error))
                })
            }

        }
    }

    override fun onClick(id: Int, position: Int) {
        buildDialog(id, position)
    }

    private fun buildDialog(id: Int, position: Int) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.delete_message)

        dialog.deleteMessageBut.setOnClickListener {
            dialog.deleteProgress.visibility = View.VISIBLE
            dialog.deleteMessageBut.visibility = View.GONE
            viewModel.deleteMessage(id).observe(this, Observer { result ->
                dialog.deleteProgress.visibility = View.GONE
                dialog.deleteMessageBut.visibility = View.VISIBLE
                if (result != null) {
                    if (result.result == true) {
                        dialog.dismiss()
                        adapter?.removeMessage(position)
                        adapter?.notifyDataSetChanged()
                    }
                    makeToast(this, result.errorMesage.toString())
                } else
                    makeToast(this, resources.getString(R.string.error))
            })

        }
        dialog.show()

    }

    private fun getMessage(): HashMap<String, String> {
        val userId = intent?.extras?.getString("user2_id")
        val message = messageEditText.text.toString()
        val map = HashMap<String, String>()
        map["message"] = message
        map["user2_id"] = userId.toString()
        return map
    }

    private fun isValidMessage(): Boolean {
        val message = messageEditText.text.toString()
        return if (message.isEmpty()) {
            messageEditText.error = resources.getString(R.string.requird)
            false
        } else
            true
    }

    override fun onDestroy() {
        mHandler?.removeCallbacks(mStatusChecker)
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = SharedPreferenceUtil(newBase!!).getString(LANG, "ar")
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase, "$lang"))
    }
}
