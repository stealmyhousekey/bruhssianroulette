package com.stelmo.brtest

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import java.io.File
import java.io.IOException
import java.util.*

class PopupActivity : AppCompatActivity() {
    val picList = mutableListOf<File>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)
        var rando = getRandomImage()
        val image = findViewById(R.id.popup_iv) as? ImageView
        image?.setImageURI(null)
        image?.setImageURI(rando)

        val closeButton = findViewById(R.id.popup_btnClose) as ImageButton

        closeButton.setOnClickListener{
            val intent = intent
            finish()
        }
    }

    //pull random image from user's device (currently pulling from /DCIM/ and subdirectories)
    fun getRandomImage(): Uri? {
        //root directory
        var dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath

        //subdirectory walking
        var picturesDirectory = File(dirName)
        var listFiles = displayDirectoryContents(picturesDirectory)

        val allowedExtensions = arrayOf<String>("jpg", "png", "gif", "jpeg")

        var r = Random()
        var randomPicture = listFiles[r.nextInt(listFiles.size)]

        //only allow image files to be used
        for (extension in allowedExtensions)
        {
            if (!randomPicture.getName().toLowerCase().endsWith(extension))
            {
                randomPicture = listFiles[r.nextInt(listFiles.size)]
            }
        }

        val pictureUri = Uri.fromFile(randomPicture)

        System.out.println("chosen file: " + randomPicture.toString())
        System.out.println("file uri: " + pictureUri)

        return pictureUri
    }

    //walk through (sub)directories and return list of all files
    fun displayDirectoryContents(dir: File): MutableList<File> {
        var i = 0 //counter var
        try {
            val files = dir.listFiles()
            for (file in files) {
                if (file.isDirectory()) {
                    if(!file.name.contains(".thumbnails")){
                        displayDirectoryContents(file)
                    }

                } else {
                    if(!picList.contains(file)) //don't add duplicates
                        picList += file
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        for(files in picList){
            //println(files.canonicalPath)
            i++
        }
        println("Total number of files found: " + i)
        return picList

    }

}
