package com.raxdenstudios.app.tmdb.view.component

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient

internal class TMDBWebViewWrapper(
    private val webView: WebView,
) {

    companion object {

        private const val REQUEST_TOKEN_URL = "https://www.themoviedb.org/auth/access?request_token="
        private const val APPROVE_URL = "https://www.themoviedb.org/auth/access/approve"
    }

    private lateinit var accessGranted: () -> Unit

    init {
        webView.prepare()
        webView.clear()
    }

    fun requestAccess(token: String, accessGranted: () -> Unit) {
        this.accessGranted = accessGranted
        webView.loadUrl("$REQUEST_TOKEN_URL$token")
    }

    private fun WebView.clear() {
        clearCache(true)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebView.prepare() {
        settings.javaScriptEnabled = true
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (isRedirectUri(url)) {
                    accessGranted()
                    hideWebView()
                } else showWebView()
            }

            private fun isRedirectUri(redirectUri: String): Boolean =
                redirectUri.startsWith(APPROVE_URL)

            private fun hideWebView() {
                visibility = View.GONE
            }

            private fun showWebView() {
                visibility = View.VISIBLE
            }
        }
    }
}
