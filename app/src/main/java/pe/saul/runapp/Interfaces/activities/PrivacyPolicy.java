package pe.saul.runapp.Interfaces.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import pe.saul.runapp.R;

/**
 * Created by Saul on 14/02/2018.
 */

public class PrivacyPolicy extends Activity {
    private WebView webView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.privacy_policy_activity);

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("http://www.google.com.pe");
    }
}
