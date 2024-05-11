package com.om_tat_sat.brightbirth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class Main_loading extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Intent intent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences app_language;
    int language;
    int versionCode=2;
    String versionName="2.0";
    boolean update_going_on=false;
    boolean handler_run_complete=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setStatusBarColor(ContextCompat.getColor(Main_loading.this,R.color.black));
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
        if (language==0){
            change_language("en");
        } else if (language==1) {
            change_language("hi");
        }
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            firebaseUser=firebaseAuth.getCurrentUser();
            firebaseUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    intent=new Intent(Main_loading.this, MainActivity.class);
                }else {
                    if (Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()).contains("The user account has been disabled by an administrator.")){
                        Toast.makeText(Main_loading.this,getString(R.string.try_with_another_email), Toast.LENGTH_LONG).show();
                        intent=new Intent(Main_loading.this, Loading_Page.class);
                    }else {
                        Toast.makeText(Main_loading.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        intent=new Intent(Main_loading.this, Loading_Page.class);
                    }
                    Log.e( "onComplete: >>>>>>>>>>>>>", task.getException().toString());
                }
            });
        }else{
            intent=new Intent(Main_loading.this, Loading_Page.class);
        }
        firebaseDatabase=FirebaseDatabase.getInstance("https://bright-birth-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference("VERSION");
        refresh();
    }
    public void refresh(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        if (Objects.equals(dataSnapshot.getKey(), "versionCode")){
                            if (versionCode!=Integer.parseInt(dataSnapshot.getValue()+"")){
                                update_going_on=true;
                                update();
                            }
                        } else if (Objects.equals(dataSnapshot.getKey(), "versionName")) {
                            if (!versionName.equals(dataSnapshot.getValue()+"")){
                                update_going_on=true;
                                update();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main_loading.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().postDelayed(() -> {
            handler_run_complete=true;
            if (!update_going_on){
                if (intent!=null){
                    startActivity(intent);
                    finishAffinity();
                }else {
                    retry();
                    Log.d( "refresh:--------------------------------------------","intent null");
                }

            }
        },6000);
    }
    public void retry(){
        new Handler().postDelayed(() -> {
            handler_run_complete=true;
            if (!update_going_on){
                if (intent!=null){
                    startActivity(intent);
                    finishAffinity();
                }else {
                    Toast.makeText(this, getString(R.string.Error_in_fetching_account_details), Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    retry();
                    Log.d( "refresh:--------------------------------------------","intent null");
                }

            }
        },6000);
    }
    public void update(){
        AlertDialog.Builder alert=new AlertDialog.Builder(Main_loading.this);
        alert.setTitle(getString(R.string.Update_Available))
                .setMessage(getString(R.string.Update_version));
        alert.setCancelable(false);
        alert.setPositiveButton(getString(R.string.Update), (dialog, which) -> {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            update_going_on=false;
            if (handler_run_complete){
                startActivity(intent);
                finishAffinity();
            }
            dialog.dismiss();
        });
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
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