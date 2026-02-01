package com.example.fragmentsdemo

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class Fragment1 : Fragment() {
     var frag_id = 0

    companion object {
        fun newInstance() = Fragment1()
    }

    private val viewModel: Fragment1ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val count = arguments?.getString("count").toString()
        //val count = 1
        if (count != "null") {
            frag_id = count.toInt()
        }

        val str_frag = frag_id.toString()
        val view = inflater.inflate(R.layout.fragment_fragment1, container, false)

        val to_set = view.findViewById<TextView>(R.id.fragTextView)
        to_set.setText("Fragment $str_frag")
        Log.d("Fragment Log", "Created Fragment Count: $count")

        return view
    }

    override fun onResume() {
        super.onResume()
        //updateText(frag_id.toString())
    }

    // Add a method to update the fragment's UI dynamically
    fun updateText(newCount: String) {
        view?.findViewById<TextView>(R.id.textView)?.text = "Fragment $newCount"
    }
}