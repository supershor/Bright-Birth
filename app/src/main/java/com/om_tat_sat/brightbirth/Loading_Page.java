package com.om_tat_sat.brightbirth;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Loading_Page extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    AppCompatButton sign_up;
    AppCompatButton login;
    Intent sign_up_page;
    Intent main_page;
    Intent login_page;
    FirebaseUser firebaseUser;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Loading_Page.this,R.color.black));

        //setting up intents
        sign_up_page=new Intent(Loading_Page.this, Sign_Up.class);
        login_page=new Intent(Loading_Page.this, Login.class);
        main_page=new Intent(Loading_Page.this, MainActivity.class);

        //checking is the user is signed in or not
        mediaPlayer=MediaPlayer.create(Loading_Page.this,R.raw.button_tap);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            firebaseUser=firebaseAuth.getCurrentUser();
            firebaseUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    startActivity(main_page);
                    finishAffinity();
                }else {
                    Toast.makeText(Loading_Page.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        //setting up buttons and click listener on them to jump to required layout
        sign_up=findViewById(R.id.loading_page_sign_up);
        login=findViewById(R.id.loading_page_login);
        sign_up.setOnClickListener(v -> {
            mediaPlayer.start();
            startActivity(sign_up_page);
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
        });
        login.setOnClickListener(v -> {
            mediaPlayer.start();
            startActivity(login_page);
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
        });
    }
}