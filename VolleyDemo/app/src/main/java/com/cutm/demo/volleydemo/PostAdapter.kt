package com.cutm.demo.volleydemo

import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private var posts: List<PostData>
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(
        itemView: View,
        val idText: TextView,
        val userIdText: TextView,
        val titleText: TextView,
        val bodyText: TextView
    ) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {

        Log.d("VOLLEY_DEMO", "onCreateViewHolder()")

        val layout = LinearLayout(parent.context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 20, 24, 20)
        }

        val idText = TextView(parent.context).apply {
            setTypeface(null, Typeface.BOLD)
        }

        val userIdText = TextView(parent.context)

        val titleText = TextView(parent.context).apply {
            setTypeface(null, Typeface.BOLD)
            textSize = 18f
        }

        val bodyText = TextView(parent.context)

        layout.addView(idText)
        layout.addView(userIdText)
        layout.addView(titleText)
        layout.addView(bodyText)

        return PostViewHolder(
            layout,
            idText,
            userIdText,
            titleText,
            bodyText
        )
    }


    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {

        val post = posts[position]

        Log.d(
            "VOLLEY_DEMO",
            "Binding post at position $position, id=${post.id}"
        )

        holder.idText.text = "ID: ${post.id}"
        holder.userIdText.text = "User ID: ${post.userId}"
        holder.titleText.text = post.title
        holder.bodyText.text = post.body
    }


    override fun getItemCount(): Int {
        return posts.size
    }


    fun updatePosts(newPosts: List<PostData>) {

        Log.d(
            "VOLLEY_DEMO",
            "Updating adapter with ${newPosts.size} posts"
        )

        posts = newPosts
        notifyDataSetChanged()
    }
}