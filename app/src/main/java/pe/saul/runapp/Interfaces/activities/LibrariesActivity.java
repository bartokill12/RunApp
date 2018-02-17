package pe.saul.runapp.Interfaces.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import pe.saul.runapp.R;

/**
 * Created by Saul on 14/02/2018.
 */

public class LibrariesActivity extends Activity {
    private TextView textView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libraries_credits_activity);

        textView = (TextView) findViewById(R.id.librariesCredits);

        textView.setText(Html.fromHtml("<div><a href=\"https://developers.google" +
                ".com/android/guides/overview\">Google Play Services</a></div>\n" +
                "<div><a href=\"http://developer.android.com/tools/support-library/features" +
                ".html#v7-appcompat\">v7 appcompat library</a></div>\n" +
                "<div><a href=\"http://developer.android.com/tools/support-library/features" +
                ".html#design\">Design Support Library</a></div>"));
        // Activate links
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
