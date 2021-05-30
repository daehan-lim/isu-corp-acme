package ca.isucorp.acme.ui.directions

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityGetDirectionsBinding
import ca.isucorp.acme.ui.workticket.EXTRA_ADDRESS
import ca.isucorp.acme.util.DEFAULT_GO_BACK_ANIMATION
import ca.isucorp.acme.util.goBackWithAnimation
import ca.isucorp.acme.util.setUpInActivity
import com.afollestad.materialdialogs.MaterialDialog

const val BASE_URL = "https://www.google.com/maps"
const val SEARCH_ENDPOINT = "/search/"
const val SEARCH_URL = BASE_URL + SEARCH_ENDPOINT

const val LOCATION_PERMISSION_CODE = 101
class GetDirectionsActivity : AppCompatActivity() {
    private var mGeoLocationCallback: GeolocationPermissions.Callback? = null
    private var mGeoLocationRequestOrigin: String? = null
    private lateinit var binding: ActivityGetDirectionsBinding
    private var connectionOk = true
    private var isTabletSize = false
    private lateinit var mapUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetDirectionsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        isTabletSize = resources.getBoolean(R.bool.isTablet)

        isTabletSize = false //remove

        val toolbar = binding.layoutSimpleAppBar.toolbar
        val toolBarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.get_directions)
        toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)

        val address = intent.getStringExtra(EXTRA_ADDRESS)
        mapUrl = when(address) {
            null -> BASE_URL
            else ->{
                SEARCH_URL + address
            }
        }

        setUpWebView()


    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = false
            settings.useWideViewPort = false
            settings.domStorageEnabled = true


            if(!isTabletSize) {
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
            } else {
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                this.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
                this.isScrollbarFadingEnabled = false
                setDesktopMode()
            }


            webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                    mGeoLocationRequestOrigin = null
                    mGeoLocationCallback = null
                    if (ContextCompat.checkSelfPermission(this@GetDirectionsActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this@GetDirectionsActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            MaterialDialog(this@GetDirectionsActivity)
                                .title(text = getString(R.string.location_permission_needed))
                                .message(text = getString(R.string.permission_location_rationale))
                                .positiveButton(R.string.accept) {
                                    mGeoLocationRequestOrigin = origin
                                    mGeoLocationCallback = callback
                                    requestLocationPermission()
                                }
                                .show()

                        } else {
                            // No explanation needed, we can request the permission.
                            mGeoLocationRequestOrigin = origin
                            mGeoLocationCallback = callback
                            requestLocationPermission()
                        }
                    } else {
                        // Tell the WebView that permission has been granted
                        callback.invoke(origin, true, false)
                    }
                }
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        if (url.startsWith("mailto:", 0)) {
                            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(url)))
                            return true
                        }
                        if (!url.contains(BASE_URL)) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            startActivity(intent)
                            return true
                        } else {
                            visibility = View.GONE
                            binding.loadingAnimation.loadingAnimation.visibility = View.VISIBLE
                        }
                    }
                    return false
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (url != null) {
                        if(connectionOk) {
                            binding.loadingAnimation.loadingAnimation.visibility = View.GONE
                            binding.noConnectionLayout.noConnectionLayout.visibility = View.GONE
                            visibility = View.VISIBLE
                            binding.swipeRefreshLayout.isEnabled = false
                        } else {
                            connectionOk = true
                        }
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                    connectionOk = false
                    binding.noConnectionLayout.noConnectionLayout.visibility = View.VISIBLE
                    binding.loadingAnimation.loadingAnimation.visibility = View.GONE
                    this@apply.visibility = View.GONE
                    binding.swipeRefreshLayout.isEnabled = true
                }

            }


            binding.swipeRefreshLayout.setOnRefreshListener {
                if(binding.loadingAnimation.loadingAnimation.visibility == View.GONE) {
                    binding.loadingAnimation.loadingAnimation.visibility = View.VISIBLE
                    binding.noConnectionLayout.noConnectionLayout.visibility = View.GONE
                    this.loadUrl(mapUrl)
                } else {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }

            this.loadUrl(mapUrl)
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this@GetDirectionsActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE
        )
    }

    private fun setDesktopMode() {
        var newUserAgent = binding.webView.settings.userAgentString
        try {
            val ua = binding.webView.settings.userAgentString
            val androidOSString = binding.webView.settings.userAgentString.substring(
                ua.indexOf("("),
                ua.indexOf(")") + 1
            )
            newUserAgent = binding.webView.settings.userAgentString.replace(
                androidOSString,
                "(X11; Linux x86_64)"
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.webView.settings.userAgentString = newUserAgent
    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()) {
            binding.noConnectionLayout.noConnectionLayout.visibility = View.GONE
            binding.webView.goBack()
        } else {
            super.onBackPressed()
            goBackWithAnimation(this, DEFAULT_GO_BACK_ANIMATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    mGeoLocationCallback?.invoke(mGeoLocationRequestOrigin, true, false)
                } else {
                    // permission denied. functionality that depends on this permission.
                    mGeoLocationCallback?.invoke(mGeoLocationRequestOrigin, false, false)
                }
            }
        }
    }
}