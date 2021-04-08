package com.raxdenstudios.commons.property

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class SharedPreferencesDelegate<T : Any>(
  private val preferences: SharedPreferences,
  private val key: (KProperty<*>) -> String = KProperty<*>::name,
  private val defaultValue: T,
  private val onAfterGetOperation: (value: T) -> Unit = {},
  private val onAfterSetOperation: (value: T) -> Unit = {}
) : ReadWriteProperty<Any, T> {

  override fun getValue(
    thisRef: Any,
    property: KProperty<*>
  ): T {
    val key = key(property)
    val value = when (defaultValue) {
      is Boolean -> preferences.getBoolean(key, defaultValue)
      is Long -> preferences.getLong(key, defaultValue)
      is String -> preferences.getString(key, defaultValue)
      else -> IllegalStateException("type not supported")
    } as T
    onAfterGetOperation(value)
    return value
  }

  override fun setValue(
    thisRef: Any,
    property: KProperty<*>,
    value: T
  ) {
    preferences.edit {
      when (defaultValue) {
        is Boolean -> putBoolean(key(property), value as Boolean)
        is Long -> putLong(key(property), value as Long)
        is String -> putString(key(property), value as String)
      }
    }
    onAfterSetOperation(value)
  }
}
