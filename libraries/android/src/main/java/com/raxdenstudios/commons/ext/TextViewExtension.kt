package com.raxdenstudios.commons.ext

import android.annotation.TargetApi
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import androidx.core.net.toUri
import com.raxdenstudios.commons.util.SDK

@Suppress("DEPRECATION")
@TargetApi(23)
fun TextView.setCompatTextAppearance(resId: Int) {
  if (SDK.hasMarshmallow()) setTextAppearance(resId)
  else setTextAppearance(context, resId)
}

fun TextView.setHtml(html: String, listener: OnHyperlinkClickListener? = null) {
  if (TextUtils.isEmpty(html)) text = ""
  else {
    var htmlContent = html
    val sequence = htmlContent.toHtml()
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    if (urls != null && urls.isNotEmpty())
      for (span in urls)
        makeLinkClickable(
          strBuilder,
          span,
          listener
        )
    else {
      var hasLinks = false
      for (word in htmlContent.split(" ".toRegex())) {
        if (word.isUrl()) {
          hasLinks = true
          htmlContent = htmlContent.replace(word, "<a href='$word'>$word</a>")
        }
      }
      if (hasLinks) {
        setHtml(htmlContent, listener)
        movementMethod = LinkMovementMethod.getInstance()
        return
      }
    }
    text = strBuilder
  }
}

private fun makeLinkClickable(
  strBuilder: SpannableStringBuilder,
  span: URLSpan,
  listener: OnHyperlinkClickListener?
) {
  val start = strBuilder.getSpanStart(span)
  val end = strBuilder.getSpanEnd(span)
  val flags = strBuilder.getSpanFlags(span)
  val clickable = object : ClickableSpan() {
    override fun onClick(view: View) {
      listener?.onHyperlinkClick(span.url.toUri())
    }
  }
  strBuilder.setSpan(clickable, start, end, flags)
  strBuilder.removeSpan(span)
}

interface OnHyperlinkClickListener {
  fun onHyperlinkClick(uri: Uri)
}
