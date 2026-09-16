package com.cutm.demo.retrofitdemo

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Root LinearLayout
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER
        linearLayout.setPadding(40, 40, 40, 40)
        // ADD DATA button
        val addButton = Button(this)
        addButton.text = "ADD DATA"
        // LIST DATA button
        val listButton = Button(this)
        listButton.text = "LIST DATA"
        // Add buttons to LinearLayout
        linearLayout.addView(
            addButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        linearLayout.addView(
            listButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        // Set Activity content
        setContentView(linearLayout)
        // ADD DATA
        addButton.setOnClickListener {
            val intent = Intent(
                this,
                AddPostActivity::class.java
            )
            startActivity(intent)
        }


        // LIST DATA
        listButton.setOnClickListener {
            listData()
        }
    }


    private fun listData() {
        RetrofitClient.api.getPosts()
            .enqueue(object : Callback<List<PostObject>> {
                override fun onResponse(
                    call: Call<List<PostObject>>,
                    response: Response<List<PostObject>>
                ) {
                    if (response.isSuccessful) {
                        val posts = response.body()
                        Log.d(
                            "RETROFIT",
                            "Posts received: ${posts?.size}"
                        )
                        if (posts != null) {
                            showPostsDialog(posts)
                        }
                    } else {
                        Log.e(
                            "RETROFIT",
                            "HTTP Error: ${response.code()}"
                        )
                    }
                }

                override fun onFailure(
                    call: Call<List<PostObject>>,
                    t: Throwable
                ) {
                    Log.e(
                        "RETROFIT",
                        "GET request failed",
                        t
                    )
                }
            })
    }


    private fun showPostsDialog(
        posts: List<PostObject>
    ) {
        // Vertical layout for dialog content
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(
            30,
            20,
            30,
            20
        )

        // Add every post
        posts.forEach { post ->
            val textView = TextView(this)
            textView.text = """
                ID       : ${post.id}
                USER ID  : ${post.userId}
                TITLE    : ${post.title}
                BODY     : ${post.body}
                
            """.trimIndent()
            textView.textSize = 16f
            layout.addView(
                textView,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }
        // ScrollView for many posts
        val scrollView = ScrollView(this)
        scrollView.addView(layout)
        // Create dialog
        AlertDialog.Builder(this)
            .setTitle("Posts")
            .setView(scrollView)
            .setPositiveButton("CLOSE", null)
            .show()
    }
}