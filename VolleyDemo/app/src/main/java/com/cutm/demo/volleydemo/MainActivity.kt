package com.cutm.demo.volleydemo

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley


class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var posts = mutableListOf<PostData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        setContentView(root)

        //CREATING SPINNER AND ADDING IT TO THE ROOT
        val spinner = Spinner(this)

        recyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        //=============
        // Spinner data
        val spinnerItems = arrayOf(
            "ID Ascending",
            "ID Descending"
        )

        // Adapter for Spinner
        val spinnerAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            spinnerItems
        )

        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = spinnerAdapter

        // Add Spinner to LinearLayout
        root.addView(
            spinner,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        Log.d("LOGS","Created the spinner and added it")
        //=======

        requestQueue = Volley.newRequestQueue(this)
        Log.d("VOLLEY_DEMO", "Volley RequestQueue created")

        fetchPosts()

        root.addView(recyclerView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            ))

        postAdapter = PostAdapter(emptyList())

        recyclerView.adapter = postAdapter

        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    p2: Int,
                    p3: Long
                ) {

                    when (p2) {

                        0 -> {
                            Log.d(
                                "VOLLEY_DEMO",
                                "Spinner: ID Ascending"
                            )

                            posts.sortBy { it.id }

                            postAdapter.updatePosts(posts)
                        }

                        1 -> {
                            Log.d(
                                "VOLLEY_DEMO",
                                "Spinner: ID Descending"
                            )

                            posts.sortByDescending { it.id }

                            postAdapter.updatePosts(posts)
                        }
                    }
                }



                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.d(
                        "VOLLEY_DEMO",
                        "Spinner: Nothing selected"
                    )
                }
            }
    }

    private fun fetchPosts() {

        val url = "https://jsonplaceholder.typicode.com/posts"

        Log.d("VOLLEY_DEMO", "Requesting URL: $url")

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                Log.d(
                    "VOLLEY_DEMO",
                    "Response received. Number of posts: ${response.length()}"
                )
                posts.clear()
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val post = PostData(
                        userId = jsonObject.getInt("userId"),
                        id = jsonObject.getInt("id"),
                        title = jsonObject.getString("title"),
                        body = jsonObject.getString("body")
                    )
                    posts.add(post)
                    Log.d(
                        "VOLLEY_DEMO",
                        "Received post: id=${post.id}"
                    )
                }

                // Sort by ID
                posts.sortBy { it.id }
                Log.d(
                    "VOLLEY_DEMO",
                    "Posts sorted by ID"
                )

                postAdapter.updatePosts(posts)
            },

            { error ->
                Log.e(
                    "VOLLEY_DEMO",
                    "Volley error: ${error.message}",
                    error
                )
            }
        )

        requestQueue.add(request)
        Log.d("VOLLEY_DEMO", "Request added to queue")
    }
}