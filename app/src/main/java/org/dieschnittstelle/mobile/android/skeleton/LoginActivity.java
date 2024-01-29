package org.dieschnittstelle.mobile.android.skeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.User;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {
    // https://www.codebrainer.com/blog/registration-form-in-android-check-email-is-valid-is-empty
    EditText usernameField;
    EditText passwordField;
    FloatingActionButton loginButton;

    ProgressBar progressBar;
    boolean userAuthenticated;

    private TextWatcher loginTextWatcher = new TextWatcher() {
        // https://gist.github.com/codinginflow/52db7d909c179e9e175dd8da322cb79e
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String usernameInput = usernameField.getText().toString().trim();
            String passwordInput = passwordField.getText().toString().trim();

            loginButton.setEnabled(checkValidtyOfEmailAddress() && checkValidityOfPassword());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (((ToDoApplication)getApplication()).isOfflineMode()) {
            startActivity(new Intent(this,OverviewActivity.class));
        }

        setContentView(R.layout.activity_login);

        this.userAuthenticated = false;

        setupUI();
        setupListeners();

        Retrofit webapiBase = new Retrofit.Builder()
                .baseUrl("http://192.168.2.225:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webapiBase.create(RetrofitToDoCRUDOperationsImpl.ToDoResource.class);
    }
    private void setupUI() {
        usernameField = findViewById(R.id.text_login_email);
        passwordField = findViewById(R.id.text_login_password);
        progressBar = findViewById(R.id.login_progressBar);
        loginButton = findViewById(R.id.login_button);

        usernameField.addTextChangedListener(loginTextWatcher);
        passwordField.addTextChangedListener(loginTextWatcher);
    }
    private void setupListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkValidtyOfEmailAddress() && checkValidityOfPassword()) {
                    String usernameValue = usernameField.getText().toString();
                    String passwordValue = passwordField.getText().toString();

                    authenticateUser(usernameValue,passwordValue);
 //               }
            }
        });
    }

    private boolean checkValidtyOfEmailAddress() {

        if (isEmpty(usernameField)) {
            usernameField.setError("You must enter username to login!");
            return false;
        } else {
            if (!isEmail(usernameField)) {
                usernameField.setError("Enter valid email!");
                return false;
            }
        }
        return true;
    }
    private boolean checkValidityOfPassword() {

        if (isEmpty(passwordField)) {
            passwordField.setError("You must enter password to login!");
            return false;
        } else {
            String passwordString = passwordField.getText().toString();
            if (passwordString.length() != 6) {
                passwordField.setError("Password must be exactly 6 chars long!");
                return false;
            }
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            if (!pattern.matcher(passwordString).matches()) {
                passwordField.setError("Password must be entirely numeric!");
            }
        }
        return true;

    }

    private void authenticateUser(String usernameValue, String passwordValue) {
        if (progressBar != null) {
            progressBar.setVisibility((View.VISIBLE));
        }
        new Thread(() -> {
            try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (Exception e) {
                    Log.i(LoginActivity.class.getSimpleName(),"Authentication-Sleep made some problems");
                }
            if (((ToDoApplication)getApplication()).getCRUDOperations().authenticateUser(new User(usernameValue, passwordValue))) {
                this.userAuthenticated = true;
            }
            this.runOnUiThread(() -> {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                if (userAuthenticated) {
                    Log.i(LoginActivity.class.getSimpleName(), "User logged in successfully. Status: " + userAuthenticated);
                    Intent i = new Intent(LoginActivity.this, OverviewActivity.class);
                    startActivity(i);
                    this.finish();
                } else {
                    Toast t = Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT);
                    t.show();
                }
             });
        }).start();
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
