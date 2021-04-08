package com.raxdenstudios.commons.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

sealed class AdvancedPreferences(
  private val sharedPreferences: SharedPreferences,
  private val gson: Gson
) {

  private val editor: Editor = Editor()

  data class Default(
    val context: Context,
    val gson: Gson = Gson()
  ) : AdvancedPreferences(PreferenceManager.getDefaultSharedPreferences(context), gson)

  /**
   * File creation mode: the default mode, where the created file can only be accessed by the
   * calling application (or all applications sharing the same user ID).
   */
  data class Private(
    val context: Context,
    val name: String = "privateSharePreferences",
    val gson: Gson = Gson()
  ) : AdvancedPreferences(context.getSharedPreferences(name, Context.MODE_PRIVATE), gson)

  inner class Editor {

    private val sharedPreferencesEditor: SharedPreferences.Editor = sharedPreferences.edit()

    fun remove(key: String): Editor = sharedPreferencesEditor.remove(key).let { this }

    fun commit() = sharedPreferencesEditor.commit()

    fun apply() = sharedPreferencesEditor.apply()

    fun clear(): Editor = sharedPreferencesEditor.clear().let { this }

    @Suppress("UNCHECKED_CAST")
    fun put(key: String, value: Any): Editor = when (value) {
      is Int -> sharedPreferencesEditor.putInt(key, value)
      is String -> sharedPreferencesEditor.putString(key, value)
      is Boolean -> sharedPreferencesEditor.putBoolean(key, value)
      is Float -> sharedPreferencesEditor.putFloat(key, value)
      is Long -> sharedPreferencesEditor.putLong(key, value)
      is Set<*> -> sharedPreferencesEditor.putStringSet(key, value as Set<String>)
      is JSONObject -> sharedPreferencesEditor.putString(key, value.toString())
      is JSONArray -> sharedPreferencesEditor.putString(key, value.toString())
      else -> sharedPreferencesEditor.putString(key, gson.toJson(value).toString())
    }.let { this }
  }

  fun contains(key: String): Boolean = sharedPreferences.contains(key)

  fun getAll(): Map<String, Any?> = sharedPreferences.all

  fun edit(): Editor = editor

  @Suppress("UNCHECKED_CAST")
  fun get(key: String, defaultValue: Any): Any = when (defaultValue) {
    is Int -> sharedPreferences.getInt(key, defaultValue)
    is String -> sharedPreferences.getString(key, defaultValue) as String
    is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
    is Float -> sharedPreferences.getFloat(key, defaultValue)
    is Long -> sharedPreferences.getLong(key, defaultValue)
    is Set<*> -> sharedPreferences.getStringSet(key, defaultValue as Set<String>) as Set<String>
    is JSONObject -> JSONObject(sharedPreferences.getString(key, defaultValue.toString()) ?: "")
    is JSONArray -> JSONArray(sharedPreferences.getString(key, defaultValue.toString()))
    else -> sharedPreferences.getString(key, null)?.let {
      gson.fromJson(it, defaultValue::class.java)
    } ?: defaultValue
  }
}
