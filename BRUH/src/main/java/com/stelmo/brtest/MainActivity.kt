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
import android.widget.*
import com.stelmo.wheelview.WheelView
import com.stelmo.wheelview.adapter.WheelArrayAdapter
import java.io.File

import java.util.ArrayList
import java.util.Random

class MainActivity : Activity() {

    //track player wins and losses
    var winCounter = 0
    var lossCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //var randomImage = getRandomImage()

        var i = intent
        if(i.extras != null){
            winCounter += i.getIntExtra("wc", 0)
            lossCounter +=  i.getIntExtra("lc", 0)
        }

        var winView = findViewById(R.id.winStats) as TextView
        winView.setText("Wins: " + winCounter)

        var lossView = findViewById(R.id.lossStats) as TextView
        lossView.setText("Losses: " + lossCounter)

        System.out.println("Wins: " + winCounter)
        System.out.println("Losses: " + lossCounter)

        isReadStoragePermissionGranted()

        val wheelView = findViewById<View>(R.id.wheelview) as WheelView
        val fire_button = findViewById<Button>(R.id.fire_button)
        val reset_button = findViewById<Button>(R.id.reset_button)
        val menu_button = findViewById<Button>(R.id.menu_button)
        var wonSlots = mutableListOf<Int>()
        val loss = losingItem()


        //create data for the adapter, assign all slots same randomized color
        val entries = ArrayList<Map.Entry<String, Int>>(ITEM_COUNT)
        val entry = MaterialColor.random(this, "\\D*_500$")
        val empty_entry = MaterialColor.random(this, "\\D*_500$")

        println("color: " + entry)
        println("Empty color: " + empty_entry)

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


        //used to debug
        wheelView.onWheelItemClickListener = WheelView.OnWheelItemClickListener { parent, position, isSelected ->
//            val msg: String
//
//            if (position == loss)
//                msg = "Loaded"
//            else
//                msg = "Safe"
//
//            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }

        fire_button.setOnClickListener {
            wheelView.stopWheel()
            if (wheelView.selectedPosition == loss){
                //Toast.makeText(applicationContext, "YOU LOSE!", Toast.LENGTH_SHORT).show()//display text on loss
                lossCounter++
                var intent = intent
                intent.putExtra("wc", winCounter)
                intent.putExtra("lc", lossCounter)
                finish()
                startActivity(intent)
                intent = Intent(baseContext, PopupActivity::class.java)
                startActivity(intent)
            }

            //if slot aleady played, mark as such with color and count
            else if(!wonSlots.contains(wheelView.selectedPosition)){
                //Toast.makeText(applicationContext, "You survived!", Toast.LENGTH_LONG).show()//display win text
                winCounter++
                wonSlots.add(wheelView.selectedPosition)
                entries[wheelView.selectedPosition] = empty_entry
                wheelView.adapter = MaterialColorAdapter(entries)

            }

            else
                Toast.makeText(applicationContext, "You have already played this slot", Toast.LENGTH_LONG).show()//no easy point farming

            System.out.println("Wins: " + winCounter)
            System.out.println("Losses: " + lossCounter)

            var winView = findViewById(R.id.winStats) as TextView
            winView.setText("Wins: " + winCounter)


            var lossView = findViewById(R.id.lossStats) as TextView
            lossView.setText("Losses: " + lossCounter)

        }


        reset_button.setOnClickListener {
            val intent = intent
            intent.putExtra("wc", winCounter)
            intent.putExtra("lc", lossCounter)
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

    companion object {
        private val ITEM_COUNT = 6
    }
}



