package iljazovic.bruno.fer.hr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Main activity. Shows a form for dividing two numbers, button to send the result to {@link
 * ShowActivity} and a button to {@link ComposeMailActivity} for composing an email.
 *
 * @author bruno
 */
public class LifecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);

        final EditText inputFirst = findViewById(R.id.input_first);
        final EditText inputSecond = findViewById(R.id.input_second);
        final TextView labelResult = findViewById(R.id.label_result);
        Button btnCalculate = findViewById(R.id.btn_calculate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = inputFirst.getText().toString();
                String second = inputSecond.getText().toString();

                int firstNumber = 0;
                int secondNumber = 0;

                try {
                    firstNumber = Integer.parseInt(first);
                    secondNumber = Integer.parseInt(second);
                } catch (NumberFormatException ex) {
                }

                if (secondNumber != 0) {
                    labelResult.setText(String.valueOf((double) firstNumber / secondNumber));
                } else {
                    labelResult.setText(R.string.invalid_operation);
                }
            }
        });

        Button btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LifecycleActivity.this, ShowActivity.class);

                Bundle extras = new Bundle();
                extras.putString(getString(R.string.result), labelResult.getText().toString());
                intent.putExtras(extras);

                startActivity(intent);
            }
        });

        Button btnEmail = findViewById(R.id.btn_email_form);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LifecycleActivity.this, ComposeMailActivity.class);

                startActivity(intent);
            }
        });

        Log.d("Lifecycle", "Pozvao oncreate");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lifecycle", "Pozvao onpause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle", "Pozvao onresume");
    }
}
