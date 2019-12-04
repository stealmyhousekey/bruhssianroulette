package com.stelmo.brtest

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.stelmo.wheelview.WheelView
import com.stelmo.wheelview.adapter.WheelArrayAdapter

import java.util.ArrayList
import java.util.Random
import java.util.Map.Entry

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            if (wheelView.selectedPosition == loss)
                Toast.makeText(applicationContext, "*BANGU*", Toast.LENGTH_LONG).show()//display the text of button1
            else
                Toast.makeText(applicationContext, "*clicko*", Toast.LENGTH_LONG).show()//display the text of button1
        }


        reset_button.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }

        menu_button.setOnClickListener {
            val intent = Intent(baseContext, HomeActivity::class.java)
            //                EditText editText = (EditText) findViewById(R.id.editText);
            //                String message = editText.getText().toString();
            startActivity(intent)
        }

        //initialise the selection drawable with the first contrast color
        wheelView.setSelectionColor(getContrastColor(entries[0]))

    }

    // select a losing value TODO also select image from the losing player's gallery
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

    companion object {

        private val ITEM_COUNT = 6
    }
}
