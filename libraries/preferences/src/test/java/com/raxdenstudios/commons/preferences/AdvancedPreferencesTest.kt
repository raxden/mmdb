package com.raxdenstudios.commons.preferences

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AdvancedPreferencesTest {

  private lateinit var defaultPreferences: AdvancedPreferences
  private lateinit var privatePreferences: AdvancedPreferences

  @Before
  fun setUp() {
    val context: Context = ApplicationProvider.getApplicationContext()
    defaultPreferences = AdvancedPreferences.Default(context)
    privatePreferences = AdvancedPreferences.Private(context)
  }

  @Test
  fun `persist a random value with apply`() {
    defaultPreferences.edit { put("key", 23) }

    assertEquals(
      23,
      defaultPreferences.get("key", -1)
    )
  }

  @Test
  fun `persist a random value with commit`() {
    defaultPreferences.edit(true) { put("key", 23) }

    assertEquals(
      23,
      defaultPreferences.get("key", -1)
    )
  }

  @Test
  fun `persist a integer value`() {
    defaultPreferences.edit { put("key", 23) }

    assertEquals(
      23,
      defaultPreferences.get("key", -1)
    )
  }

  @Test
  fun `persist a string value`() {
    defaultPreferences.edit { put("key", "test_23") }

    assertEquals(
      "test_23",
      defaultPreferences.get("key", "no_value")
    )
  }

  @Test
  fun `persist a float value`() {
    defaultPreferences.edit { put("key", 23.0f) }

    assertEquals(
      23.0f,
      defaultPreferences.get("key", -0.1f)
    )
  }

  @Test
  fun `persist a boolean value`() {
    defaultPreferences.edit { put("key", true) }

    assertEquals(
      true,
      defaultPreferences.get("key", false)
    )
  }

  @Test
  fun `persist a long value`() {
    defaultPreferences.edit { put("key", 23L) }

    assertEquals(
      23L,
      defaultPreferences.get("key", -1L)
    )
  }

  @Test
  fun `persist a set of values`() {
    defaultPreferences.edit { put("set_key", setOf("test_20", "test_21", "test_22", "test_23")) }

    assertEquals(
      setOf("test_20", "test_21", "test_22", "test_23"),
      defaultPreferences.get("set_key", emptySet<String>())
    )
  }

  @Test
  fun `persist a jsonObject value`() {
    defaultPreferences.edit { put("set_json_object", JSONObject("{\"key\": 23}")) }

    assertEquals(
      JSONObject("{\"key\": 23}").toString(),
      defaultPreferences.get("set_json_object", JSONObject().toString())
    )
  }

  @Test
  fun `persist a jsonArray value`() {
    defaultPreferences.edit { put("set_json_array", JSONArray("[{\"key\": 23}]")) }

    assertEquals(
      JSONArray("[{\"key\": 23}]").toString(),
      defaultPreferences.get("set_json_array", JSONArray().toString())
    )
  }

  @Test
  fun `persist a random object value`() {
    defaultPreferences.edit { put("set_test_object", TestObject("key", "value")) }

    assertEquals(
      TestObject("key", "value"),
      defaultPreferences.get("set_test_object", TestObject("empty", "empty"))
    )
  }

  @Test
  fun `return default object when persisted object doesn't exists`() {
    assertEquals(
      TestObject("empty", "empty"),
      defaultPreferences.get("set_test_object", TestObject("empty", "empty"))
    )
  }

  @Test
  fun `persist a random value with a key and verify that preferences contains a value with the key used`() {
    defaultPreferences.edit { put("key_23", "test_23") }

    assertEquals(
      true,
      defaultPreferences.contains("key_23")
    )
  }

  @Test
  fun `persist a random values, remove one of them and verify that value was removed`() {
    defaultPreferences.edit {
      put("key_23", "test_23")
      put("key_24", "test_24")
      remove("key_24")
    }

    assertEquals(
      "test_23",
      defaultPreferences.get("key_23", "no_value")
    )
    assertEquals(
      "no_value",
      defaultPreferences.get("key_24", "no_value")
    )
    assertEquals(
      false,
      defaultPreferences.contains("key_24")
    )
  }

  @Test
  fun `persist a random values, clear all preferences and verify that preferences is cleared`() {
    defaultPreferences.edit {
      put("key_23", "test_23")
      put("key_24", "test_24")
      put("key_25", "test_25")
      put("key_26", "test_26")
    }
    defaultPreferences.edit { clear() }

    assertEquals(false, defaultPreferences.contains("key_23"))
    assertEquals(false, defaultPreferences.contains("key_24"))
    assertEquals(false, defaultPreferences.contains("key_25"))
    assertEquals(false, defaultPreferences.contains("key_26"))
  }

  @Test
  fun `persist a random values, get all preferences and verify that preferences exists`() {
    defaultPreferences.edit {
      put("key", "string")
      put("key", 23)
      put("key", 12f)
      put("key", 42L)
    }

    assertEquals(
      mapOf(
        "key" to "string",
        "key" to 23,
        "key" to 12f,
        "key" to 42L
      ),
      defaultPreferences.getAll()
    )
  }

  data class TestObject(val key: String, val value: String) : Comparable<TestObject> {

    override fun compareTo(other: TestObject): Int = when {
      key == other.key && value == other.value -> 0
      else -> -1
    }
  }
}
