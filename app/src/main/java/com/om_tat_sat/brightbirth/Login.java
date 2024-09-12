package com.om_tat_sat.brightbirth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText email;
    EditText password;
    AppCompatButton forgot_password;
    AppCompatButton login;
    AppCompatButton sign_up;
    FirebaseAuth firebaseAuth;
    String issue;
    MediaPlayer mediaPlayer;
    SharedPreferences app_language;
    int language=0;
    Intent main_page;
    Intent signup_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Login.this,R.color.candle));

        //setting intents
        main_page=new Intent(Login.this, MainActivity.class);
        signup_page=new Intent(Login.this, Sign_Up.class);

        //checking is the user is signed in or not
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(main_page);
            finishAffinity();
        }

        //initializing elements
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
        if (language==0){
            change_language("en");
        } else if (language==1) {
            change_language("hi");
        }
        mediaPlayer=MediaPlayer.create(Login.this,R.raw.button_tap);
        email=findViewById(R.id.email_information_login_page);
        password=findViewById(R.id.password_information_login_page);
        forgot_password=findViewById(R.id.forgot_password_login_page);
        login=findViewById(R.id.login_at_login_page);
        sign_up=findViewById(R.id.sign_up_at_login_page);

        login.setOnClickListener(v -> {
            mediaPlayer.start();
            if (check_fields()){
                Toast.makeText(Login.this,issue, Toast.LENGTH_SHORT).show();
            }else {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        startActivity(main_page);
                        finishAffinity();
                    }else {
                        if (Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()).contains("The user account has been disabled by an administrator.")){
                            Toast.makeText(Login.this,getString(R.string.try_with_another_email), Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.e( "onComplete: >>>>>>>>>>>>>", task.getException().toString());

                    }
                });
            }
        });
        sign_up.setOnClickListener(v -> {
            mediaPlayer.start();
            startActivity(signup_page);
            finish();
        });
        forgot_password.setOnClickListener(v -> {
            mediaPlayer.start();
            EditText editText=new EditText(Login.this);
            final AlertDialog.Builder alertDialog=new AlertDialog.Builder(Login.this);
            alertDialog.setView(editText);
            alertDialog.setCancelable(false);
            alertDialog.setTitle(getString(R.string.reset_password))
                    .setMessage(getString(R.string.enter_full_email_login));
            alertDialog.setPositiveButton(getString(R.string.Send), (dialog, which) -> {
                if (editText.getText() == null || editText.getText().toString().isEmpty() || !editText.getText().toString().contains("@") || editText.getText().toString().contains(" ")) {
                    Toast.makeText(Login.this, getString(R.string.Invalid_email), Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, getString(R.string.Reset_link_sent), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            alertDialog.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        });
    }
    public boolean check_fields(){
        if (email.getText()==null||password.getText()==null||email.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
            issue=getString(R.string.enter_all_fields);
            return true;
        }else if (!email.getText().toString().contains("@")){
            issue=getString(R.string.Invalid_email);
            return true;
        }else if (email.getText().toString().contains(" ")){
            issue=getString(R.string.email_contains_spaces);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }
    public void change_language(String language){
        Resources resources=this.getResources();
        Configuration configuration=resources.getConfiguration();
        Locale locale=new Locale(language);
        locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}