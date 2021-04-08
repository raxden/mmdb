package com.raxdenstudios.commons.ext

import android.text.Html
import android.text.Spanned
import android.util.Base64
import android.util.Patterns
import com.raxdenstudios.commons.util.SDK
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun String.isUrl() = Patterns.WEB_URL.matcher(this).matches()

fun String.isPhone() = Patterns.PHONE.matcher(this).matches()

fun String.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

@Suppress("DEPRECATION")
fun String.toHtml(): Spanned =
  if (SDK.hasNougat()) Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
  else Html.fromHtml(this)

fun String.encodeToBase64(charset: Charset = Charsets.UTF_8, flags: Int = Base64.NO_WRAP): String {
  var result: ByteArray? = null
  try {
    result = toByteArray(charset)
  } catch (e: UnsupportedEncodingException) {
  }
  return Base64.encodeToString(result, flags)
}

fun String.decodeFromBase64(
  charset: Charset = Charsets.UTF_8,
  flags: Int = Base64.NO_WRAP
): String {
  var decodedString = ""
  try {
    decodedString = String(Base64.decode(this, flags), charset)
  } catch (e: UnsupportedEncodingException) {
  }
  return decodedString
}

fun String.ifEmptyThen(text: String): String {
  return if (this.isEmpty()) text
  else this
}

@Suppress("MagicNumber")
fun String.toMD5(): String {
  val md = MessageDigest.getInstance("MD5")
  return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun String.toSHA256(charset: Charset = Charsets.UTF_8): String {
  return hash(this, "SHA-256", charset)
}

fun String.toSHA512(charset: Charset = Charsets.UTF_8): String {
  return hash(this, "SHA-512", charset)
}

private fun hash(key: String, algorithm: String, charset: Charset): String {
  return digest(key, algorithm, charset)?.let { bytesToHexString(it) } ?: ""
}

private fun digest(key: String, algorithm: String, charset: Charset = Charsets.UTF_8): ByteArray? {
  var digest: ByteArray? = null
  try {
    val messageDigest = MessageDigest.getInstance(algorithm)
    messageDigest.update(key.toByteArray(charset))
    digest = messageDigest.digest()
  } catch (e: NoSuchAlgorithmException) {
  } catch (e: UnsupportedEncodingException) {
  }
  return digest
}

private fun bytesToHexString(bytes: ByteArray): String {
  // http://stackoverflow.com/questions/332079
  val sb = StringBuilder()
  for (i in bytes.indices) {
    val hex = Integer.toHexString(0xFF and bytes[i].toInt())
    if (hex.length == 1) {
      sb.append('0')
    }
    sb.append(hex)
  }
  return sb.toString()
}
