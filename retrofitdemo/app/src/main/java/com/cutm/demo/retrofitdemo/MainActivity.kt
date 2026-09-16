package com.cutm.demo.retrofitdemo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cutm.demo.retrofirdemo.PostObject
import com.cutm.demo.retrofirdemo.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Root LinearLayout
        val linearLayout = LinearLayout(this)

        linearLayout.orientation = LinearLayout.VERTICAL

        // ScrollView
        val scrollView = ScrollView(this)

        val horizontalScrollView =
            HorizontalScrollView(this)

        // TableLayout
        val tableLayout = TableLayout(this)

        tableLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Put Horizontal Scroll View inside ScrollView
        scrollView.addView(horizontalScrollView)

        //Put Table Layout inside Horizontal Scroll View
        horizontalScrollView.addView(tableLayout)

        // Put ScrollView inside LinearLayout
        linearLayout.addView(
            scrollView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )

        // Set root layout as Activity content
        setContentView(linearLayout)

        // Create table header
        addHeaderRow(tableLayout)

        // Retrofit GET request
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

                        posts?.forEach { post ->

                            addPostRow(
                                tableLayout,
                                post
                            )
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
                        "Request failed",
                        t
                    )
                }
            })
    }


    private fun addHeaderRow(
        tableLayout: TableLayout
    ) {

        val row = TableRow(this)

        row.gravity = Gravity.CENTER

        addTextView(
            row,
            "ID",
            true
        )

        addTextView(
            row,
            "USER ID",
            true
        )

        addTextView(
            row,
            "TITLE",
            true
        )

        addTextView(
            row,
            "BODY",
            true
        )

        tableLayout.addView(row)
    }


    private fun addPostRow(
        tableLayout: TableLayout,
        post: PostObject
    ) {

        val row = TableRow(this)

        row.gravity = Gravity.CENTER

        addTextView(
            row,
            post.id.toString(),
            false
        )

        addTextView(
            row,
            post.userId.toString(),
            false
        )

        addTextView(
            row,
            post.title,
            false
        )

        addTextView(
            row,
            post.body,
            false
        )

        tableLayout.addView(row)
    }


    private fun addTextView(
        row: TableRow,
        text: String,
        isHeader: Boolean
    ) {

        val textView = TextView(this)

        textView.text = text

        textView.textSize = 18f

        textView.setTextColor(Color.BLACK)

        textView.gravity = Gravity.CENTER

        if (isHeader) {
            textView.setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(
            16,
            16,
            16,
            16
        )

        textView.layoutParams = params

        row.addView(textView)
    }
}