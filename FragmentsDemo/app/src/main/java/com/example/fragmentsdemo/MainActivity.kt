package com.example.fragmentsdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //ADDING FRAGMENTS AND AT THE SAME TO BACKSTACK
        findViewById<Button>(R.id.b1).setOnClickListener {
            counter++
            val bund = Bundle()
            bund.putString("count", counter.toString())
            val frag = Fragment1()
            frag.arguments = bund
            val ft = supportFragmentManager.beginTransaction()

            //REPLACING THE FRAGMENT - FRAGMENT WILL BE REPLACED AND WILL ALSO BE ASSIGNED A TAG
            ft.replace(R.id.fcv, frag, counter.toString())
            ft.addToBackStack(counter.toString())
            ft.commit()
            Log.d("Fragment Log", "Back Stack Entry Count: ${supportFragmentManager.backStackEntryCount}")
            findViewById<TextView>(R.id.counterView).setText(counter.toString())
        }

        findViewById<Button>(R.id.b2).setOnClickListener {
            val frag_id = findViewById<TextView>(R.id.fargReqNo).text.toString()
            Log.d("Fragment Log", "Entered Fragment Tag: $frag_id")

            try {
                if (frag_id.toInt() < counter) {
                    var ft = supportFragmentManager.beginTransaction()
                    val fragment = supportFragmentManager.findFragmentById(R.id.fcv)
                    // Fragment exists but is detached, attach it
                    if (fragment != null) {
                        ft.remove(fragment)
                    }
                    ft.commit()

                    val fr = supportFragmentManager.findFragmentByTag(frag_id)  as? Fragment1// Find fragment by tag
                    if (fr != null) {
                        ft = supportFragmentManager.beginTransaction()
                        if (fr.isAdded) {
                            //fr.updateText(frag_id)
                            // Fragment is already attached, replace it
                            ft.replace(R.id.fcv, fr)
                        } else {
                            //fr.frag_id = frag_id.toInt()
                            ft.add(fr, frag_id)
                        }
                        ft.commit()
                        Log.d("Fragment Log", "FRAGMENT $frag_id ADDED. Fragment transaction committed successfully.")
                    } else {
                        Log.e("Fragment Log", "Fragment not found for the given tag.")
                        Toast.makeText(this, "Fragment not found.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Number is either equal to the last fragment or greater than the number of fragments created",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("Req Counter", "Exception occurred: ${e.message}", e)
                Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
            Log.d("Fragment Log", "Back Stack Entry Count: ${supportFragmentManager.backStackEntryCount}")
        }
    }
}