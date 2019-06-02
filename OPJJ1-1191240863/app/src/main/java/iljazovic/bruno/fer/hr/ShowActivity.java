package iljazovic.bruno.fer.hr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Shows the result passed from {@link LifecycleActivity}.
 *
 * @author bruno
 */
public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        String result = getIntent().getExtras().getString(getString(R.string.result));

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        final TextView inputText = findViewById(R.id.input_text);

        inputText.setText(result);
    }
}
