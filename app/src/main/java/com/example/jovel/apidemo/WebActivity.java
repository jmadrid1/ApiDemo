package com.example.jovel.apidemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.jovel.apidemo.config.Config;

public class WebActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String redirect = Config.INSTA_OAUTH +
                        "?client_id=" +
                        Config.CLIENT_ID +
                        Config.REDIRECT_URL +
                        Config.SCOPE +
                        Config.RESPONSE_TYPE;

        //Presents a WebView and then grabs the access token
        webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                String fragment = "#access_token=";
                int start = url.indexOf(fragment);
                if (start > -1) {

                    String token = url.substring(start + fragment.length(), url.length());

                    Intent intent = new Intent(WebActivity.this, MainActivity.class);
                    intent.putExtra("token", token);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    overridePendingTransition(0,0);

                    startActivity(intent);
                }
            }
        });

        webview.loadUrl(redirect);
    }
}
