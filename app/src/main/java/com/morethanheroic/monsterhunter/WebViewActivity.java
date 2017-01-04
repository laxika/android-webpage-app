package com.morethanheroic.monsterhunter;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class WebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupGlobalCookieStorage();
        setupContentContainer();
        setupWebView();
    }

    private void setupGlobalCookieStorage() {
        CookieManager.setAcceptFileSchemeCookies(true);
        CookieManager.getInstance().setAcceptCookie(true);
    }

    private void setupContentContainer() {
        setContentView(R.layout.activity_web_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupWebView() {
        final WebView webView = locateIndexWebView();

        setupWebViewCookieStorage(webView);
        setupWebViewSettings(webView);
        setupWebViewContent(webView);
    }

    private WebView locateIndexWebView() {
        return (WebView) findViewById(R.id.webview);
    }

    private void setupWebViewCookieStorage(final WebView webView) {
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
    }

    private void setupWebViewSettings(final WebView webView) {
        final WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(false);
    }

    private void setupWebViewContent(final WebView webView) {
        webView.loadDataWithBaseURL("file:///android_asset/", getIndexHtml(), "text/html", "utf-8", "about:blank");
    }

    private String getIndexHtml() {
        try (InputStream is = getAssets().open("index.html")) {
            return IOUtils.toString(is);
        } catch (IOException e) {
            Log.e("IndexHtmlLoader", "Failed to load the index html.", e);

            throw new RuntimeException("Failed to load the index html.");
        }
    }
}