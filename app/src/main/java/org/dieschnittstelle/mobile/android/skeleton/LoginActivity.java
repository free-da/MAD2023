package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    // https://www.codebrainer.com/blog/registration-form-in-android-check-email-is-valid-is-empty
    EditText usernameField;
    EditText passwordField;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupUI();
        setupListeners();
    }
    private void setupUI() {
        usernameField = findViewById(R.id.text_login_email);
        passwordField = findViewById(R.id.text_login_password);
        login = findViewById(R.id.login_button);
    }
    private void setupListeners() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUsername();
            }
        });
    }
    void checkUsername() {
        boolean isValid = true;
        if (isEmpty(usernameField)) {
            usernameField.setError("You must enter username to login!");
            isValid = false;
        } else {
            if (!isEmail(usernameField)) {
                usernameField.setError("Enter valid email!");
                isValid = false;
            }
        }

        if (isEmpty(passwordField)) {
            passwordField.setError("You must enter password to login!");
            isValid = false;
        } else {
            if (passwordField.getText().toString().length() < 4) {
                passwordField.setError("Password must be at least 4 chars long!");
                isValid = false;
            }
        }

        //check email and password
        //IMPORTANT: here should be call to backend or safer function for local check; For example simple check is cool
        //For example simple check is cool
        if (isValid) {
            String usernameValue = usernameField.getText().toString();
            String passwordValue = passwordField.getText().toString();
            if (usernameValue.equals("test@test.com") && passwordValue.equals("password1234")) {
                //everything checked we open new activity
                Intent i = new Intent(LoginActivity.this, OverviewActivity.class);
                startActivity(i);
                //we close this activity
                this.finish();
            } else {
                Toast t = Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}
