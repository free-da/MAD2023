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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.dieschnittstelle.mobile.android.skeleton.model.RetrofitToDoCRUDOperationsImpl;
import org.dieschnittstelle.mobile.android.skeleton.model.ToDo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class LoginActivity extends Activity {
    // https://www.codebrainer.com/blog/registration-form-in-android-check-email-is-valid-is-empty
    EditText usernameField;
    EditText passwordField;
    FloatingActionButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        if (isValid) {
            String usernameValue = usernameField.getText().toString();
            String passwordValue = passwordField.getText().toString();


            /*if (usernameValue.equals("test@test.com") && passwordValue.equals("password1234")) {
                //everything checked we open new activity
                Intent i = new Intent(LoginActivity.this, OverviewActivity.class);
                startActivity(i);
                //we close this activity
                this.finish();
            } else {
                Toast t = Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT);
                t.show();
            }*/
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
