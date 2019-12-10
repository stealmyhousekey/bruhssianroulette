package com.stelmo.brtest

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.stelmo.wheelview.WheelView
import com.stelmo.wheelview.adapter.WheelArrayAdapter
import java.io.File

import java.util.ArrayList
import java.util.Random

class MainActivity : Activity() {

    var handler = Handler()
    var counter = 0
    private var mImageView: ImageView? = null
    private var currentBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isReadStoragePermissionGranted()

        val wheelView = findViewById<View>(R.id.wheelview) as WheelView
        val fire_button = findViewById<Button>(R.id.fire_button)
        val reset_button = findViewById<Button>(R.id.reset_button)
        val menu_button = findViewById<Button>(R.id.menu_button)
        val loss = losingItem()


        //create data for the adapter, assign all slots same randomized color
        val entries = ArrayList<Map.Entry<String, Int>>(ITEM_COUNT)
        val entry = MaterialColor.random(this, "\\D*_500$")
        for (i in 0 until ITEM_COUNT) {
            entries.add(entry)
        }

        //populate the adapter, that knows how to draw each item (as you would do with a ListAdapter)
        wheelView.adapter = MaterialColorAdapter(entries)

        //a listener for receiving a callback for when the item closest to the selection angle changes
        wheelView.setOnWheelItemSelectedListener { parent, itemDrawable, position ->
            //get the item at this position
            val selectedEntry = (parent.adapter as MaterialColorAdapter).getItem(position)
            parent.setSelectionColor(getContrastColor(selectedEntry))
        }

        wheelView.onWheelItemClickListener = WheelView.OnWheelItemClickListener { parent, position, isSelected ->
            val msg: String

            if (position == loss)
                msg = "Loaded"
            else
                msg = "Empty"

            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }

        fire_button.setOnClickListener {
            wheelView.stopWheel()
            if (wheelView.selectedPosition == loss){
                //Toast.makeText(applicationContext, "*BANG*", Toast.LENGTH_LONG).show()//display the text of button1
                //showDialog()
                getRandomImage()
            }
            else
                Toast.makeText(applicationContext, "*click*", Toast.LENGTH_LONG).show()//display the text of button1
        }


        reset_button.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }

        menu_button.setOnClickListener {
            val intent = Intent(baseContext, HomeActivity::class.java)
            startActivity(intent)
        }

        //initialise the selection drawable with the first contrast color
        wheelView.setSelectionColor(getContrastColor(entries[0]))

    }

    // select a losing value
    fun losingItem(): Int {
        val r = Random()
        return r.nextInt(6)
    }

    //get the materials darker contrast
    private fun getContrastColor(entry: Map.Entry<String, Int>): Int {
        val colorName = MaterialColor.getColorName(entry)
        return MaterialColor.getContrastColor(colorName)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    internal class MaterialColorAdapter(entries: ArrayList<Map.Entry<String, Int>>) : WheelArrayAdapter<Map.Entry<String, Int>>(entries) {

        override fun getDrawable(position: Int): Drawable {
            val drawable = arrayOf(createOvalDrawable(getItem(position).value), TextDrawable(position.toString()))
            return LayerDrawable(drawable)
        }

        private fun createOvalDrawable(color: Int): Drawable {
            val shapeDrawable = ShapeDrawable(OvalShape())
            shapeDrawable.paint.color = color
            return shapeDrawable
        }
    }

    //image popup handler
    private fun showDialog() {

        // custom dialog
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)

        //random image from gallery TODO make it actually swap the image

        // set the custom dialog components - text, image and button
        val close = dialog.findViewById(R.id.btnClose) as ImageButton

        // dialog close button. also resets game after closing
        close.setOnClickListener {
            dialog.dismiss()
            val intent = intent
            finish()
            startActivity(intent)
        }
        dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    //TODO make select a random image WHY WON'T YOU WORK
    fun getRandomImage(){
        //val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        var dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        var listOfFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list()
        var picturesDirectory = File(dirName)
        val listFiles = picturesDirectory.listFiles()


//        for (file in listFiles)
//        {
//            System.out.println("file: " + file.getCanonicalPath())
//        }


        val r = Random()
        val randomPicture = listFiles[r.nextInt(listFiles.size)]
        val pictureUri = Uri.fromFile(randomPicture)

        System.out.println("chosen file: " + randomPicture.toString())
        System.out.println("file uri: " + pictureUri)


        var bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureUri)

        //val f = File(getRealPathFromURI(pictureUri))
        //val d = Drawable.createFromPath(pictureUri)
        val image = findViewById(R.id.iv_main) as? ImageView
        //image?.background = d
        image?.setImageURI(null)
        image?.setImageURI(pictureUri)

    }

    //checks and requests device read perms
    fun isReadStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), 3)
                return false
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }

    private fun getRealPathFromURI(contentURI:Uri):String {
        val cursor = getContentResolver().query(contentURI, null, null, null, null)
        if (cursor == null)
        { // Source is Dropbox or other similar local file path
            return contentURI.getPath()
        }
        else
        {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
    }

    companion object {
        private val ITEM_COUNT = 6
    }
}



