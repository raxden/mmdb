package com.raxdenstudios.commons.ext

import android.content.SharedPreferences
import com.raxdenstudios.commons.property.SharedPreferencesDelegate
import java.util.*
import kotlin.reflect.KProperty

fun SharedPreferences.boolean(
  key: (KProperty<*>) -> String = KProperty<*>::name,
  defaultValue: Boolean = false,
  onAfterGetOperation: (value: Boolean) -> Unit = {},
  onAfterSetOperation: (value: Boolean) -> Unit = {}
) = SharedPreferencesDelegate(this, key, defaultValue, onAfterGetOperation, onAfterSetOperation)

fun SharedPreferences.long(
  key: (KProperty<*>) -> String = KProperty<*>::name,
  defaultValue: Long = 0L,
  onAfterGetOperation: (value: Long) -> Unit = {},
  onAfterSetOperation: (value: Long) -> Unit = {}
) = SharedPreferencesDelegate(this, key, defaultValue, onAfterGetOperation, onAfterSetOperation)

fun SharedPreferences.string(
  key: (KProperty<*>) -> String = KProperty<*>::name,
  defaultValue: String = "",
  onAfterGetOperation: (value: String) -> Unit = {},
  onAfterSetOperation: (value: String) -> Unit = {}
) = SharedPreferencesDelegate(this, key, defaultValue, onAfterGetOperation, onAfterSetOperation)

fun SharedPreferences.getStringNotNull(key: String, defaultValue: String): String =
  getString(key, defaultValue) ?: defaultValue
