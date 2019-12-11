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
import java.util.*

class PopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)
        var rando = getRandomImage()
        val image = findViewById(R.id.popup_iv) as? ImageView
        //image?.background = d
        image?.setImageURI(null)
        image?.setImageURI(rando)


        val closeButton = findViewById(R.id.popup_btnClose) as ImageButton


        closeButton.setOnClickListener{
            val intent = intent
            finish()
        }


    }


    fun getRandomImage(): Uri? {
        var dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath
        dirName += "/Camera/" //use pics from camera
        var picturesDirectory = File(dirName)
        var listFiles = picturesDirectory.listFiles()

        dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        dirName += "/Screenshots/" //use pics from screenshots TODO read from more folders
        picturesDirectory = File(dirName)
        listFiles += picturesDirectory.listFiles()

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

        return pictureUri //TODO make sure picture isn't null, and also make typecheck work
    }

}
