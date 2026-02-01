package com.example.filereadingapplication

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var cv: EditText
    private lateinit var rcv: EditText
    private lateinit var fnv: EditText

    private val REQUEST_CODE_SAVE_FILE = 100
    private val REQUEST_CODE_OPEN_FILE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cv = findViewById(R.id.contentsView)
        rcv = findViewById(R.id.retrievedContentsView)
        fnv = findViewById(R.id.fileNameView)

        val sbtn = findViewById<Button>(R.id.saveBtn)
        val rbtn = findViewById<Button>(R.id.retrieveBtn)

        sbtn.setOnClickListener {
            //saveContentsToAppStorage()
            //saveContentsToExternalStorage()
            saveContentsToUsingSAF()
        }

        rbtn.setOnClickListener {
            //readFileFromAppStorage()
            //readFileFromExternalStorage()
            openContentsToUsingSAF()
        }
    }

    // INTERNAL STORAGE IS THE SPACE WITHIN THE APP'S PRIVATE DIRECTORY,
    //      AND FILES STORED HERE CANNOT BE ACCESSED BY OTHER APPS.
    // ✔️ THIS IS THE SAFEST OPTION FOR STORING SENSITIVE DATA.
    // openFileOutput() & openFileInput() ARE USED TO WRITE AND READ DATA
    //      TO FILES IN Context CLASS. WE ALSO USE java.io PACKAGE

    private fun saveContentsToAppStorage(){
        val saveData = cv.getText().toString()
        val fileName = fnv.getText().toString()
        if (fileName.isEmpty()){
            Toast.makeText(this,
                "No Filename Entered",
                Toast.LENGTH_SHORT).show()
        }else if(saveData.isEmpty()){
            Toast.makeText(this,
                "No Contents to save",
                Toast.LENGTH_SHORT).show()
        }else {
            openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(saveData.toByteArray())
            }
        }
    }

    private fun readFileFromAppStorage(){
        val fileName = fnv.getText().toString()
        val f1 = File(filesDir, fileName)
        if (f1.exists()) {
            val fileContent = openFileInput(fileName).bufferedReader().use {
                it.readText()
            }
            Log.d("FileStorageLog", "File stored in $filesDir")
            rcv.setText(fileContent)
        }else{
            Toast.makeText(this,
                "No file with the name $fileName exists",
                Toast.LENGTH_SHORT).show()
        }
    }

    // EXTERNAL STORAGE REFERS TO LOCATIONS LIKE:
    // ✔️ SHARED MEDIA STORAGE (PHOTOS, DOCUMENTS, DOWNLOADS)
    // ✔️ APP-SPECIFIC EXTERNAL STORAGE (PRIVATE FOLDERS WITHIN
    //                  /ANDROID/DATA/{PACKAGE-NAME}/FILES/)
    // ✔️ REMOVABLE STORAGE (SD CARDS, IF AVAILABLE)
    // FILES SAVED IN EXTERNAL STORAGE MAY BE ACCESSIBLE BY OTHER APPS
    //      IF STORED IN PUBLIC DIRECTORIES.

    private fun saveContentsToExternalStorage(){
        val saveData = cv.getText().toString()
        val fileName = fnv.getText().toString()
        //val fldr = getExternalFilesDir(null)
        //val fldr = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val fldr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Log.d("FileStorageLog", "Folder from to file is being stored: $fldr")

        if (fileName.isEmpty()){
            Toast.makeText(this,
                "No Filename Entered",
                Toast.LENGTH_SHORT).show()
        }else if(saveData.isEmpty()){
            Toast.makeText(this,
                "No Contents to save",
                Toast.LENGTH_SHORT).show()
        }else {
            val file = File(fldr, fileName)
            file.writeText(saveData)
        }
    }

    private fun readFileFromExternalStorage(){
        val fileName = fnv.getText().toString()
        //val fldr = getExternalFilesDir(null)
        //val fldr = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val fldr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Log.d("FileStorageLog", "Folder from which file is being opened: $fldr")
        val f1 = File(fldr, fileName)
        if (f1.exists()) {
            val fileContent = f1.readText()
            rcv.setText(fileContent)
        }else{
            Toast.makeText(this,
                "No file with the name $fileName exists",
                Toast.LENGTH_SHORT).show()
        }
    }

    //USING STORAGE ACCESS FRAMEWORK (SAF)
    //SAF FACILITATES IN CREATING FILE DIALOGUES
    private fun saveContentsToUsingSAF(){
        val saveData = cv.getText().toString()
        val fileName = fnv.getText().toString()
        if (fileName.isEmpty()){
            Toast.makeText(this,
                "No Filename Entered",
                Toast.LENGTH_SHORT).show()
        }else if(saveData.isEmpty()){
            Toast.makeText(this,
                "No Contents to save",
                Toast.LENGTH_SHORT).show()
        }else {
            // Launch file picker for saving the file
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain"
            }

            startActivityForResult(intent, REQUEST_CODE_SAVE_FILE) // Handle the result in onActivityResult
        }
    }

    private fun openContentsToUsingSAF(){
        val saveData = cv.getText().toString()
        val fileName = fnv.getText().toString()
        if (fileName.isEmpty()){
            Toast.makeText(this,
                "No Filename Entered",
                Toast.LENGTH_SHORT).show()
        }else if(saveData.isEmpty()){
            Toast.makeText(this,
                "No Contents to save",
                Toast.LENGTH_SHORT).show()
        }else {
            // Launch file picker for saving the file
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain"
            }

            startActivityForResult(intent, REQUEST_CODE_OPEN_FILE) // Handle the result in onActivityResult
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REQUEST_CODE_SAVE_FILE && resultCode == Activity.RESULT_OK){
            data?.data?.let{uri ->
                try{
                    val saveData = cv.getText().toString()
                    contentResolver.openOutputStream(uri)?.write(saveData.toByteArray())
                }catch(e: Exception){
                    Log.e("FileStorageLog", "Error saving file", e)
                }
            }
        }else if (requestCode == REQUEST_CODE_OPEN_FILE && resultCode == Activity.RESULT_OK){
            data?.data?.let{uri ->
                try{
                    contentResolver.openInputStream(uri)?.bufferedReader().use{
                        val fc = it?.readText()
                        rcv.setText(fc)
                    }
                }catch(e: Exception){
                    Log.e("FileStorageLog", "Error opening file", e)
                }
            }
        }
    }


    //USING MEDIASTORE FOR STORING FILES
    private fun saveContentsUsingMediaStore(){
        val saveData = cv.getText().toString()
        val fileName = fnv.getText().toString()

        // DEFINE METADATA (name, type, location)
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName) // File name
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain") // File type
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS) // Save in "Downloads"
        }

        //CREATE AN EMPTY FILE AND RETURN ITS URI
        val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        Log.d("FileStorageLog", "New file opened is ${uri.toString()}")

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write(saveData.toByteArray()) // OPENING AN OUTPUT STREAM AND WRITE CONTENT
            }
        }
    }


}