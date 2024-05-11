package com.om_tat_sat.brightbirth;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Recyclerview_Interface {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    SharedPreferences app_language;
    int language=0;
    FloatingActionButton add_new_date;
    TextView english;
    TextView hindi;
    CheckBox english_checkbox;
    CheckBox hindi_checkbox;
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    EditText name;
    DatePicker datePicker;
    MediaPlayer mediaPlayer;
    String issue="";
    Spinner spinner;
    ArrayList<name_bday_holder>arrayList;
    ArrayList<String>arr;
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
        arrayList= new ArrayList<>();
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
                arr=new ArrayList<>();
                if (language==0){
                    arr.add("Select Zodiac");
                } else if (language==1) {
                    arr.add("राशि चुनें");
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
                spinner=view2.findViewById(R.id.spinner_at_add_new);
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(MainActivity.this,R.layout.text_spinner,arr);
                arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                name=view2.findViewById(R.id.name_information_add_new);
                datePicker=view2.findViewById(R.id.date_information_add_new);
                alertDialog.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (check()){
                            Toast.makeText(MainActivity.this, issue, Toast.LENGTH_SHORT).show();
                        }
                        else if (spinner.getSelectedItemPosition()==0){
                            Toast.makeText(MainActivity.this, getString(R.string.spinner_message), Toast.LENGTH_SHORT).show();
                        }else if (key.contains(name.getText().toString()+"_"+datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear())) {
                            Toast.makeText(MainActivity.this, getString(R.string.already_exists_with_same_name_and_date), Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, getString(R.string.try_changing_fields), Toast.LENGTH_SHORT).show();
                        } else{
                            HashMap<String,String>hashMap=new HashMap<>();
                            hashMap.put("name",name.getText().toString());
                            hashMap.put("zodiac",spinner.getSelectedItem()+"");
                            Log.e( "onClick: ",spinner.getSelectedItem()+"");
                            hashMap.put("date",datePicker.getDayOfMonth()+"_"+(datePicker.getMonth()+1)+"_"+datePicker.getYear());
                            databaseReference.child(name.getText().toString()+"_"+(datePicker.getDayOfMonth()+1)+"_"+datePicker.getMonth()+"_"+datePicker.getYear()).setValue(hashMap)
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
                alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
    public void receive_data(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e( "onDataChange:--------------------", snapshot.getValue()+"");
                if (snapshot.getValue()!=null){
                    arrayList.clear();
                    for (DataSnapshot ds:snapshot.getChildren()) {
                        key.add(ds.getKey()+"");
                        String zodiac;
                        if (ds.child("zodiac").getValue()!=null){
                            zodiac=ds.child("zodiac").getValue()+"";
                        }else {
                            zodiac="-1";
                        }
                        arrayList.add(new name_bday_holder(ds.child("name").getValue()+"",ds.child("date").getValue()+"",zodiac));
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
        Intent intent=new Intent(MainActivity.this, Birth_data.class);
        intent.putExtra("name",arrayList.get(i).getName());
        intent.putExtra("birth_date",arrayList.get(i).getDate());
        intent.putExtra("birth_month",arrayList.get(i).getMonth());
        intent.putExtra("birth_year",arrayList.get(i).getYear());
        intent.putExtra(",zodiac",arrayList.get(i).getZodiac());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
    }
    public void changeLanguage(String  language){
        Resources resources=this.getResources();
        Configuration configuration=resources.getConfiguration();
        Locale locale=new Locale(language);
        locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mediaPlayer.start();

        if (item.getItemId()==R.id.logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.logout))
                    .setMessage(getString(R.string.sure_to_logout))
                    .setPositiveButton(getString(R.string.logout), (dialog, which) -> {
                        mediaPlayer.start();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this,Loading_Page.class));
                        finishAffinity();
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mediaPlayer.start();
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }else if (item.getItemId()==R.id.report_error){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Report error on Grade ACE.");
            intent.putExtra(Intent.EXTRA_TEXT,"Hello 👋\n"+"\nThis is :-\n"+firebaseAuth.getCurrentUser().getUid()+"\n"+"(It's your I'd kindly do not edit)"+"\n\nName:-\nPhone Number:-\nError:-");
            startActivity(intent);

        }else if (item.getItemId()==R.id.contact_owner){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Contact owner of Grade ACE.");
            intent.putExtra(Intent.EXTRA_TEXT,"Hello 👋\n"+"\nThis is :-\n"+firebaseAuth.getCurrentUser().getUid()+"\n"+"(It's your I'd kindly do not edit)"+"\n\nName:-\nPhone Number:-\nReason:-");
            startActivity(intent);
        }else if (item.getItemId()==R.id.refresh){
            receive_data();
            Toast.makeText(this, "Refresh Done", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId()==R.id.change_language){
            Log.e("onOptionsItemSelected:-------------------","1");
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.change_language,null);
            english=view.findViewById(R.id.textview_english);
            hindi=view.findViewById(R.id.textview_hindi);
            english_checkbox=view.findViewById(R.id.checkbox_english);
            hindi_checkbox=view.findViewById(R.id.checkbox_hindi);
            if (language==0){
                english_checkbox.setChecked(true);
            } else if (language==1) {
                hindi_checkbox.setChecked(true);
            }
            Log.e("onOptionsItemSelected:-------------------","2");
            AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTheme);
            alert.setView(view);
            alert.setCancelable(false);
            alert.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (english_checkbox.isChecked()){
                        SharedPreferences.Editor editor=app_language.edit();
                        editor.putInt("current_language",0);
                        editor.apply();
                        changeLanguage("en");
                    } else if (hindi_checkbox.isChecked()) {
                        SharedPreferences.Editor editor=app_language.edit();
                        editor.putInt("current_language",1);
                        editor.apply();
                        changeLanguage("hi");
                    }
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    finishAffinity();
                }
            });
            Log.e("onOptionsItemSelected:-------------------","3");
            alert.show();
            Log.e("onOptionsItemSelected:-------------------","4");
            english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    english_checkbox.setChecked(true);
                    hindi_checkbox.setChecked(false);
                }
            });
            english_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    english_checkbox.setChecked(true);
                    hindi_checkbox.setChecked(false);
                }
            });
            hindi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hindi_checkbox.setChecked(true);
                    english_checkbox.setChecked(false);
                }
            });
            hindi_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hindi_checkbox.setChecked(true);
                    english_checkbox.setChecked(false);
                }
            });
            Log.e("onOptionsItemSelected:-------------------","5");
        }
        return super.onOptionsItemSelected(item);
    }
}