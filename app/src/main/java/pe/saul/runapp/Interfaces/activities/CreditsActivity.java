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

public class CreditsActivity extends Activity {

    private TextView textView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libraries_credits_activity);

        textView = (TextView) findViewById(R.id.librariesCredits);
        textView.setText(Html.fromHtml("<div>Icons made by <a href=\"http://www.freepik.com\" " +
                "title=\"Freepik\">Freepik</a> from <a href=\"http://www.flaticon.com\" " +
                "title=\"Flaticon\">www.flaticon.com</a> is licensed by <a " +
                "href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY" +
                " 3.0\">CC BY 3.0</a></div>\n" +
                "<div>Icons made by <a href=\"http://www.flaticon.com/authors/google\" " +
                "title=\"Google\">Google</a> from <a href=\"http://www.flaticon.com\" " +
                "title=\"Flaticon\">www.flaticon.com</a> is licensed by <a " +
                "href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons " +
                "BY3.0\">CC BY 3.0</a></div>\n" +
                "<div>Icons made by <a href=\"http://www.flaticon.com/authors/scott-de-jonge\" " +
                "title=\"Scott de Jonge\">Scott de Jonge</a> from <a href=\"http://www.flaticon" +
                ".com\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a " +
                "href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY" +
                " 3.0\">CC BY 3.0</a></div>"));
        // Activate links
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
