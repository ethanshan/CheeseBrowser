package net.codingpark.cheesebrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ethanshan on 8/28/14.
 */
public class WebActivity extends Activity {
    private WebView webView     = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  

        setContentView(R.layout.activity_webview);

        
        //1. Get url from intent
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("url");
        webView = (WebView)findViewById(R.id.webView);
        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        // Set WebViewClient
        webView.setWebViewClient(new CustomWebViewClient());
        // Set WebChromeClient
        webView.setWebChromeClient(new WebChromeClient());
        // Starting load the web page
        webView.loadUrl(url);
    }
    
    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}