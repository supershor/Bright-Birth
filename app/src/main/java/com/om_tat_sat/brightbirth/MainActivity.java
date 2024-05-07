package com.om_tat_sat.brightbirth;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.om_tat_sat.brightbirth.Implementations.Recyclerview_Interface;
import com.om_tat_sat.brightbirth.Recycler.Recycler;
import com.om_tat_sat.brightbirth.data_holders.name_bday_holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Recyclerview_Interface {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    SharedPreferences app_language;
    int language=0;
    AppCompatButton add_new_date;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    EditText name;
    DatePicker datePicker;
    MediaPlayer mediaPlayer;
    String issue="";
    ArrayList<name_bday_holder>arrayList;
    ArrayList<String>key;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.black));
        //tool bar setup
        toolbar=findViewById(R.id.toolbar_main_page);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, Loading_Page.class));
            finishAffinity();
        }
        //initializing
        add_new_date=findViewById(R.id.add_new_date);
        arrayList=new ArrayList<name_bday_holder>();
        key=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_main);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
        mediaPlayer= MediaPlayer.create(MainActivity.this,R.raw.button_tap);
        firebaseDatabase=FirebaseDatabase.getInstance("https://bright-birth-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("date_name_data");
        receive_data();
        add_new_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view2= LayoutInflater.from(MainActivity.this).inflate(R.layout.add_new_name_and_birthday,null);
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
                alertDialog.setView(view2);
                name=view2.findViewById(R.id.name_information_add_new);
                datePicker=view2.findViewById(R.id.date_information_add_new);
                alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (check()){
                            Toast.makeText(MainActivity.this, issue, Toast.LENGTH_SHORT).show();
                        } else if (key.contains(name.getText().toString()+"_"+datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear())) {
                            Toast.makeText(MainActivity.this, "Birthdate with same name and date already exists.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Try changing name or date", Toast.LENGTH_SHORT).show();
                        } else{
                            HashMap<String,String>hashMap=new HashMap<>();
                            hashMap.put("name",name.getText().toString());
                            hashMap.put("date",datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear());
                            databaseReference.child(name.getText().toString()+"_"+datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear()).setValue(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainActivity.this, "New Birthdate addition successful", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Log.e( "onComplete:efkjewofno;ewfoewhfoihweihdiowe", task.getException()+"");
                                                Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
    }
    public boolean check(){
        if (name.getText()==null || name.getText().toString().isEmpty()){
            issue="Enter name";
            return true;
        } else if (check2()) {
            return true;
        }
        return false;
    }
    public boolean check2(){
        String names=name.getText().toString();
        if (names.contains(".")){
            issue="Name cannot contain '.' ";
            return true;
        } else if (names.contains("#")) {
            issue="Name cannot contain '#' ";
            return true;
        }
        else if (names.contains("$")) {
            issue="Name cannot contain '$' ";
            return true;
        }else if (names.contains("[")) {
            issue="Name cannot contain '[' ";
            return true;
        }
        else if (names.contains("]")) {
            issue="Name cannot contain ']' ";
            return true;
        }
        else if (names.contains("_")) {
            issue="Name cannot contain '_' ";
            return true;
        }
        else if (names.contains("/")) {
            issue="Name cannot contain '/' ";
            return true;
        }
        return false;
    }
    public void receive_data(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e( "onDataChange:--------------------", snapshot.getValue()+"");
                if (snapshot.getValue()!=null){
                    arrayList.clear();
                    for (DataSnapshot ds:snapshot.getChildren()) {
                        key.add(ds.getKey()+"");
                        arrayList.add(new name_bday_holder(ds.child("name").getValue()+"",ds.child("date").getValue()+""));
                    }
                    Recycler recycler=new Recycler(MainActivity.this,arrayList,MainActivity.this::click);
                    Log.e( "onDataChange: ", "2222222222222222222222222");
                    recyclerView.setAdapter(recycler);
                    Log.e( "onDataChange: ", "33333333333333333333333333");
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    Log.e( "onDataChange: ", "4444444444444444444444");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        receive_data();
    }

    @Override
    public void click(int i, int j) {
        Toast.makeText(this, i+"="+j, Toast.LENGTH_SHORT).show();
    }
}