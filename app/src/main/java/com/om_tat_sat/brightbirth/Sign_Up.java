package com.om_tat_sat.brightbirth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.Objects;

public class Sign_Up extends AppCompatActivity {
    EditText email;
    EditText name;
    CheckBox checkBox;
    EditText password;
    EditText confirm_password;
    AppCompatButton save;
    AppCompatButton have_an_account;
    Intent main_page;
    Intent login;
    FirebaseAuth firebaseAuth;
    SharedPreferences app_language;
    int language=0;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MediaPlayer mediaPlayer;
    String issue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Sign_Up.this,R.color.candle));

        //setting intents
        main_page=new Intent(Sign_Up.this, MainActivity.class);
        login=new Intent(Sign_Up.this, Login.class);

        //checking is the user is signed in or not
        firebaseAuth= FirebaseAuth.getInstance();
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
        mediaPlayer=MediaPlayer.create(Sign_Up.this,R.raw.button_tap);
        name=findViewById(R.id.name_information_sign_up_page);
        email=findViewById(R.id.email_information_sign_up_page);
        password=findViewById(R.id.password_information_sign_up_page);
        confirm_password=findViewById(R.id.confirm_password_information_sign_up_page);
        checkBox=findViewById(R.id.checkbox_agree_for_terms_and_condition_at_sign_up_page);

        //FireBase initializing
        firebaseDatabase=FirebaseDatabase.getInstance("https://bright-birth-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference();

        //setting on click listener
        save=findViewById(R.id.save_account_creation_information);
        have_an_account=findViewById(R.id.already_have_an_account_sign_up_page);
        save.setOnClickListener(v -> {
            mediaPlayer.start();
            Log.e( "onCreate: >>>>>>>>>>>>>>>>>>>>>", "hbtrgfhbhfdg");
                save();
        });
        have_an_account.setOnClickListener(v -> {
            mediaPlayer.start();
            startActivity(login);
            finish();
        });
    }
    public void save(){
        if (check_fields()){
            Toast.makeText(Sign_Up.this,issue, Toast.LENGTH_SHORT).show();
        }else{
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    databaseReference.child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid()).child("Personal information").child("NAME").setValue(name.getText().toString()).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            startActivity(main_page);
                            finishAffinity();
                        }else{
                            Toast.makeText(Sign_Up.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(Sign_Up.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public boolean check_fields(){
        //checking all input fields for valid input
        if (name.getText()==null||confirm_password.getText()==null||email.getText()==null||password.getText()==null|| name.getText().toString().isEmpty() ||email.getText().toString().isEmpty()||password.getText().toString().isEmpty()||confirm_password.getText().toString().isEmpty()){
            issue=getString(R.string.enter_all_fields);
            return true;
        }else if(password.getText().length()<=8){
            issue=getString(R.string.password_greater_then_8);
            return true;
        }else if(email.getText().length()<=0){
            issue=getString(R.string.enter_valid_email);
            return true;
        }else if(name.getText().length()<=1){
            issue=getString(R.string.name_greater_then_1);
            return true;
        }else if(confirm_password.getText().length()<=8){
            issue=getString(R.string.password_greater_then_8);
            return true;
        }else if(!checkBox.isChecked()){
            issue=getString(R.string.I_agree_terms_and_condition);
            return true;
        } else if (password.getText().toString().contains(" ")) {
            issue=getString(R.string.Invalid_password_spaces);
            return true;
        }
        else if (!password.getText().toString().equals(confirm_password.getText().toString())){
            issue=getString(R.string.both_password_must_be_same);
            return true;
        } else if (!email.getText().toString().contains("@")){
            issue=getString(R.string.Invalid_email);
            return true;
        }else if (email.getText().toString().contains(" ")){
            issue=getString(R.string.Invalid_email_spaces);
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