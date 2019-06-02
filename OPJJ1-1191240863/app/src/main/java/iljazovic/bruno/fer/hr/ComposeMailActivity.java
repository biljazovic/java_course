package iljazovic.bruno.fer.hr;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Shows a form for composing an email with required address to send to, title and message of the
 * email. Clicking on the button shows a chooser for an application that can send emails.
 * <p>
 * Emails must be valid, checked by {@link android.util.Patterns#EMAIL_ADDRESS} pattern.
 *
 * @author bruno
 */
public class ComposeMailActivity extends AppCompatActivity {

    private static final String[] cc_emails = new String[]{"ana@baotic.org", "marcupic@gmail.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_mail);

        final EditText inputEmail = findViewById(R.id.input_email);
        final EditText inputTitle = findViewById(R.id.input_title);
        final EditText inputMessage = findViewById(R.id.input_message);
        Button btnEmailSend = findViewById(R.id.btn_email_send);

        btnEmailSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String title = inputTitle.getText().toString().trim();
                String message = inputMessage.getText().toString().trim();

                if (email.isEmpty() || title.isEmpty() || message.isEmpty()) {
                    Toast.makeText(
                            ComposeMailActivity.this,
                            getString(R.string.incomplete_entry_error),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(
                            ComposeMailActivity.this,
                            getString(R.string.invalid_email_error),
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.putExtra(Intent.EXTRA_CC, cc_emails);

                startActivity(Intent.createChooser(intent, getString(R.string.send_email_action)));

                finish();
            }
        });

    }
}
