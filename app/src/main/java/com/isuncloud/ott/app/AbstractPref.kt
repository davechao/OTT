package com.isuncloud.ott.app

import android.content.Context
import android.text.TextUtils

abstract class AbstractPref protected constructor(protected val context: Context, private val preferenceFileName: String) {

    open inner class BooleanPref @JvmOverloads constructor(private val key: String, private val defaultValue: Boolean = false) {

        fun get(): Boolean {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            return prefs.getBoolean(key, defaultValue)
        }

        fun set(value: Boolean) {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun remove() {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    inner class IntPref @JvmOverloads constructor(private val key: String, private val defaultValue: Int = 0) {

        fun get(): Int {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            return prefs.getInt(key, defaultValue)
        }

        fun set(value: Int) {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun remove() {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    inner class FloatPref @JvmOverloads constructor(private val key: String, private val defaultValue: Float = 0f) {

        fun get(): Float {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            return prefs.getFloat(key, defaultValue)
        }

        fun set(value: Float) {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putFloat(key, value)
            editor.apply()
        }

        fun remove() {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    inner class LongPref(private val key: String) {

        fun get(): Long {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            return prefs.getLong(key, 0)
        }

        fun set(value: Long) {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putLong(key, value)
            editor.apply()
        }

        fun remove() {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    inner class SingleShotFlag : BooleanPref {

        private val defaultValue: Boolean

        val andSet: Boolean
            get() {
                val value = get()
                set(!defaultValue)
                return value
            }

        constructor(key: String) : super(key, false) {
            defaultValue = false
        }

        constructor(key: String, init: Boolean) : super(key, init) {
            defaultValue = init
        }

        fun setToDefaultValue() {
            set(defaultValue)
        }
    }

    inner class StringPref @JvmOverloads constructor(private val key: String, private val defaultValue: String = "") {

        val isDefined: Boolean
            get() = !TextUtils.isEmpty(get())

        fun get(): String? {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            return prefs.getString(key, defaultValue)
        }

        fun set(value: String) {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun remove() {
            val prefs = context.getSharedPreferences(preferenceFileName,
                    Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.remove(key)
            editor.apply()
        }
    }
}
