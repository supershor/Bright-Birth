package com.om_tat_sat.brightbirth;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddNewData extends AppCompatActivity {

    String str_alert_message;
    ArrayList<String> arr;
    SharedPreferences app_language;
    int language=0;
    EditText name;
    DatePicker datePicker;
    ArrayList<String>key;
    Spinner spinner;
    String issue="";
    FloatingActionButton add;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setStatusBarColor(ContextCompat.getColor(AddNewData.this,R.color.candle));
        //checking if the user is logged in or not
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(AddNewData.this, Loading_Page.class));
            finishAffinity();
        }
        //initializing
        firebaseDatabase= FirebaseDatabase.getInstance("https://bright-birth-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference(firebaseAuth.getCurrentUser().getUid()).child("date_name_data");
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
        arr=new ArrayList<>();
        if (language==0){
            str_alert_message="Save this info ?";
            arr.add("Select Zodiac");


        } else if (language==1) {
            arr.add("राशि चुनें");
            str_alert_message="'यह जानकारी सहेजें ?";
        }
        arr.add("Aquarius ( कुंभ )");
        arr.add("Aries ( मेष )");
        arr.add("Cancer ( कैंसर )");
        arr.add("Capricorn ( मकर )");
        arr.add("Gemini ( मिथुन )");
        arr.add("Leo ( सिंह )");
        arr.add("Libra ( तुला )");
        arr.add("Pisces ( मीन )");
        arr.add("Sagittarius ( धनु )");
        arr.add("Scorpio ( वृश्चिक )");
        arr.add("Taurus ( वॄष )");
        arr.add("Virgo ( कन्या )");
        spinner=findViewById(R.id.spinner_at_add_new);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(AddNewData.this,R.layout.text_spinner,arr);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        name=findViewById(R.id.name_information_add_new);
        add=findViewById(R.id.add_new_info);
        datePicker=findViewById(R.id.date_information_add_new);
        datePicker.getTouchables().get( 0 ).performClick();

        Intent intent_passed=getIntent();
        key=intent_passed.getStringArrayListExtra("keys");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    Toast.makeText(AddNewData.this, issue, Toast.LENGTH_SHORT).show();
                }
                else if (spinner.getSelectedItemPosition()==0){
                    Toast.makeText(AddNewData.this, getString(R.string.spinner_message), Toast.LENGTH_SHORT).show();
                }else if (key.contains(name.getText().toString()+"_"+datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear())) {
                    Toast.makeText(AddNewData.this, getString(R.string.already_exists_with_same_name_and_date), Toast.LENGTH_SHORT).show();
                    Toast.makeText(AddNewData.this, getString(R.string.try_changing_fields), Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder alert=new AlertDialog.Builder(AddNewData.this,R.style.MyDialogTheme);
                    alert.setTitle(str_alert_message)
                            .setMessage(getString(R.string.Are_you_sure_you_want_to_save_information_with_name)+" \""+name.getText()+"\" "+getString(R.string.and_zodiac_sign)+" \""+spinner.getSelectedItem()+"\" "+getString(R.string.with_birthday)+" \""+datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getYear()+"\" ?")
                            .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    add();
                                }
                            })
                            .setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder sure_delete=new AlertDialog.Builder(AddNewData.this,R.style.MyDialogTheme);
                                    sure_delete.setTitle(getString(R.string.delete)+" ?")
                                            .setMessage(getString(R.string.sure_delete))
                                            .setPositiveButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();;
                                                }
                                            })
                                            .setNegativeButton(getString(R.string.yes_delete), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .show();
                                }
                            })
                            .setNeutralButton(getString(R.string.change), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }

            }
        });


    }
    public void add(){
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("name",name.getText().toString());
            hashMap.put("zodiac",spinner.getSelectedItem()+"");
            Log.e( "onClick: ",spinner.getSelectedItem()+"");
            hashMap.put("date",datePicker.getDayOfMonth()+"_"+(datePicker.getMonth()+1)+"_"+datePicker.getYear());
            databaseReference.child(name.getText().toString()+"_"+(datePicker.getDayOfMonth()+1)+"_"+datePicker.getMonth()+"_"+datePicker.getYear()).setValue(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddNewData.this, "New Birthdate addition successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Log.e( "onComplete:efkjewofno;ewfoewhfoihweihdiowe", task.getException()+"");
                                Toast.makeText(AddNewData.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }
    public boolean check(){
        if (name.getText()==null || name.getText().toString().isEmpty()){
            issue=getString(R.string.enter_name);
            return true;
        } else if (check2()) {
            return true;
        }
        return false;
    }
    public boolean check2(){
        String names=name.getText().toString();
        if (names.contains(".")){
            issue=getString(R.string.can_not_contain_in_name)+"  '.'";
            return true;
        } else if (names.contains("#")) {
            issue=getString(R.string.can_not_contain_in_name)+"  '#'";
            return true;
        }
        else if (names.contains("$")) {
            issue=getString(R.string.can_not_contain_in_name)+"  '$'";
            return true;
        }else if (names.contains("[")) {
            issue=getString(R.string.can_not_contain_in_name)+"  '['";
            return true;
        }
        else if (names.contains("]")) {
            issue=getString(R.string.can_not_contain_in_name)+"  ']'";
            return true;
        }
        else if (names.contains("_")) {
            issue=getString(R.string.can_not_contain_in_name)+"  '_'";
            return true;
        }
        else if (names.contains("/")) {
            issue=getString(R.string.can_not_contain_in_name)+"  '/'";
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }
}