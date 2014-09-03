package net.codingpark.cheesebrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ethanshan on 8/28/14.
 */
public class WebActivity extends Activity {
    private WebView webview     = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        //1. Get url from intent
        Intent intent = this.getIntent();
        String url = intent.getStringExtra("url");
        webview = (WebView)findViewById(R.id.webView);
        //设置WebView属性，能够执行JavaScript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        //设置web视图客户端
        webview.setWebViewClient(new CustomWebViewClient());
        //加载URL内容
        webview.loadUrl(url);
    }

    class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}