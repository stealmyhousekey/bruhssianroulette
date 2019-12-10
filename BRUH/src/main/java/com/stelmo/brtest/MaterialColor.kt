package com.stelmo.brtest

import android.content.Context

import java.util.ArrayList
import java.util.HashMap
import java.util.Random
import java.util.regex.Pattern

object MaterialColor {
    private val sRandom = Random()
    private var sMaterialHashMap: HashMap<String, Int>? = null
    private val sColorPattern = Pattern.compile("_[aA]?+\\d+")

    private fun getMaterialColors(context: Context): HashMap<String, Int> {
        val fields = R.color::class.java.fields
        val materialHashMap = HashMap<String, Int>(fields.size)
        for (field in fields) {
            if (field.type != Int::class.javaPrimitiveType) continue

            val fieldName = field.name //prone to errors but okay for a sample!
            if (fieldName.startsWith("abc") || fieldName.startsWith("material")) continue

            try {
                val resId = field.getInt(null)
                materialHashMap[fieldName] = context.resources.getColor(resId)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }

        return materialHashMap
    }

    fun random(context: Context, regex: String): Map.Entry<String, Int> {
        if (sMaterialHashMap == null) {
            sMaterialHashMap = getMaterialColors(context)
        }

        val pattern = Pattern.compile(regex)
        val materialColors = ArrayList<Map.Entry<String, Int>>()
        for (entry in sMaterialHashMap!!.entries) {
            if (!pattern.matcher(entry.key).matches()) continue
            materialColors.add(entry)
        }

        val rndIndex = sRandom.nextInt(materialColors.size)
        return materialColors[rndIndex]
    }

    fun getContrastColor(colourName: String?): Int {
        return sMaterialHashMap!![colourName + "_700"]!!
    }

    fun getColorName(entry: Map.Entry<String, Int>): String? {
        val color = entry.key
        val matcher = sColorPattern.matcher(color)
        return if (matcher.find()) {
            color.substring(0, matcher.start())
        } else null
    }
}

