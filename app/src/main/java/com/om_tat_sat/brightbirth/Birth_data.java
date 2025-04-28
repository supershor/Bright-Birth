package com.om_tat_sat.brightbirth;

import static com.google.ai.client.generativeai.BuildConfig.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.ai.client.generativeai.BuildConfig;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;

public class Birth_data extends AppCompatActivity {
    Intent intent;
    String name;
    String birth_date;
    SharedPreferences app_language;
    int language=0;
    String birth_month;
    String birth_year;
    TextView Name_data;
    TextView Name_birth_data;
    TextView Birthday_birthdate;
    TextView Birthday_Rarity_birthdate;
    TextView Day_specific_facts;
    TextView Day_specific_facts_birthdate;
    TextView Historical_Events;
    TextView Historical_Events_birthdate;
    TextView Notable_Births;
    TextView Notable_Births_birthdate;
    TextView Notable_Deaths;
    TextView Notable_Deaths_birthdate;
    TextView Hidden_Zodiac_Facts;
    TextView Hidden_Zodiac_Facts_birthdate;
    TextView zodiac_traits;
    HashMap<String ,String>hashMap_zodiac_traits;
    TextView Zodiac_traits_birthdate;
    TextView Negative_traits;
    TextView Negative_traits_birthdate;
    String language_name;
    String traits;
    String Zodiac;
    static {
        System.loadLibrary("native-lib");
    }

    public native String getEncryptedKey();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_birth_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setStatusBarColor(ContextCompat.getColor(Birth_data.this,R.color.candle));
        intent=getIntent();
        name=intent.getStringExtra("name");
        birth_date=intent.getStringExtra("birth_date");
        birth_month=intent.getStringExtra("birth_month");
        birth_year=intent.getStringExtra("birth_year");
        Zodiac=intent.getStringExtra(",zodiac");
        Name_data=findViewById(R.id.Name_birthdate);
        Name_birth_data=findViewById(R.id.name_meaning_birthdate);
        Birthday_birthdate=findViewById(R.id.Birthday_birthdate);
        Birthday_Rarity_birthdate=findViewById(R.id.Birthday_Rarity_birthdate);
        Day_specific_facts=findViewById(R.id.Day_specific_facts);
        Day_specific_facts_birthdate=findViewById(R.id.Day_specific_facts_birthdate);
        Historical_Events=findViewById(R.id.Historical_Events);
        Historical_Events_birthdate=findViewById(R.id.Historical_Events_birthdate);
        Notable_Births=findViewById(R.id.Notable_Births);
        Notable_Births_birthdate=findViewById(R.id.Notable_Births_birthdate);
        Notable_Deaths=findViewById(R.id.Notable_Deaths);
        Notable_Deaths_birthdate=findViewById(R.id.Notable_Deaths_birthdate);
        Hidden_Zodiac_Facts=findViewById(R.id.Hidden_Zodiac_Facts);
        Hidden_Zodiac_Facts_birthdate=findViewById(R.id.Hidden_Zodiac_Facts_birthdate);
        zodiac_traits=findViewById(R.id.zodiac_traits);
        Zodiac_traits_birthdate=findViewById(R.id.Zodiac_traits_birthdate);
        Negative_traits_birthdate=findViewById(R.id.Negative_traits_birthdate);
        Negative_traits=findViewById(R.id.Negative_traits);
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
        if (language==0){
            language_name="english";
        } else if (language==1) {
            language_name="hindi";
        }
        hashMap_zodiac_traits=new HashMap<>();

        name_meaning();
        Birthday_Rarity();
        Day_specific_facts();
        Historical_Events();
        Notable_Births();
        Notable_Deaths();


        Log.e( "onCreate:jdryjyrjyrdhj ", Zodiac+"");
        if (Zodiac.contains("-1")){
            Zodiac_finder(Integer.parseInt(birth_date),Integer.parseInt(birth_month));
            zodiac_traits.setText(Zodiac);
            setZodiac_traits();
            Hidden_Zodiac_Facts();
            Negative_Zodiac_traits();
        }else {
            zodiac_traits.setText(Zodiac);
            setZodiac_traits();
            Hidden_Zodiac_Facts();
            Negative_Zodiac_traits();

        }

    }
    private void Negative_Zodiac_traits() {
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Tell me some Negative traits of Zodiac "+Zodiac+" in "+language_name)
                .build();


        ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Log.e( "00000000000 ", resultText);
                Negative_traits_birthdate.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }
    public void setZodiac_traits(){
        hashMap_zodiac_traits.put("Aries ( मेष )","✧Characteristics: Dynamic, assertive, passionate.\n" +
                "✧Personality Traits: Adventurous, confident, independent.\n" +
                "✧Strengths: Leadership skills, courage, determination.\n" +
                "✧Weaknesses: Impatience, impulsiveness, short-tempered nature.");
        hashMap_zodiac_traits.put("Taurus ( वॄष )","✧Characteristics: Reliable, practical, patient.\n" +
                "✧Personality Traits: Grounded, loyal, sensual.\n" +
                "✧Strengths: Stability, persistence, strong work ethic.\n" +
                "✧Weaknesses: Stubbornness, possessiveness, resistance to change.");
        hashMap_zodiac_traits.put("Gemini ( मिथुन )","✧Characteristics: Versatile, curious, sociable.\n" +
                "✧Personality Traits: Intelligent, adaptable, expressive.\n" +
                "✧Strengths: Communication skills, versatility, and quick thinking.\n" +
                "✧Weaknesses: Restlessness, inconsistency, tendency to be superficial.");
        hashMap_zodiac_traits.put("Cancer ( कैंसर )","✧Characteristics: Nurturing, intuitive,emotional.\n" +
                "✧Personality Traits: Compassionate, empathetic, protective.\n" +
                "✧Strengths: Sensitivity, loyalty, strong intuition.\n" +
                "✧Weaknesses: Moodiness, clinginess, sensitivity to criticism.");
        hashMap_zodiac_traits.put("Leo ( सिंह )","✧Characteristics: Confident, generous, charismatic.\n" +
                "✧Personality Traits: Creative, passionate, natural leaders.\n" +
                "✧Strengths: Leadership abilities, enthusiasm, self-expression.\n" +
                "✧Weaknesses: Egotism, stubbornness, desire for attention.");
        hashMap_zodiac_traits.put("Virgo ( कन्या )","✧Characteristics: Analytical, practical, perfectionist.\n" +
                "✧Personality Traits: Detail-oriented, reliable, and hardworking.\n" +
                "✧Strengths: Attention to detail, organization, and problem-solving skills.\n" +
                "✧Weaknesses: Overcritical nature, anxiety, and the tendency to overthink.");
        hashMap_zodiac_traits.put("Libra ( तुला )","✧Characteristics: Balanced, diplomatic, harmonious.\n" +
                "✧Personality Traits: Charming, fair-minded, cooperative.\n" +
                "✧Strengths: Diplomacy, social skills, ability to see multiple perspectives.\n" +
                "✧Weaknesses: Indecisiveness, avoidance of conflict, people-pleasing tendencies.");
        hashMap_zodiac_traits.put("Scorpio ( वृश्चिक )","✧Characteristics: Intense, passionate, mysterious.\n" +
                "✧Personality Traits: Determined, brave, and perceptive.\n" +
                "✧Strengths: Emotional depth, loyalty, resourcefulness.\n" +
                "✧Weaknesses: Jealousy, possessiveness, tendency to hold grudges.");
        hashMap_zodiac_traits.put("Sagittarius ( धनु )","✧Characteristics: Adventurous, optimistic, philosophical.\n" +
                "✧Personality Traits: Independent, straightforward, intellectual.\n" +
                "✧Strengths: Optimism, enthusiasm, love for exploration.\n" +
                "✧Weaknesses: Impatience, tactlessness, a tendency towards impulsiveness.");
        hashMap_zodiac_traits.put("Capricorn ( मकर )","✧Characteristics: Ambitious, disciplined, practical.\n" +
                "✧Personality Traits: Responsible, patient, self-controlled.\n" +
                "✧Strengths: Determination, reliability, strong work ethic.\n" +
                "✧Weaknesses: Pessimism, rigidity, difficulty expressing emotions.");
        hashMap_zodiac_traits.put("Aquarius ( कुंभ )","✧Characteristics: Innovative, intellectual, humanitarian.\n" +
                "✧Personality Traits: Independent, open-minded, visionary.\n" +
                "✧Strengths: Creativity, intellect, humanitarian pursuits.\n" +
                "✧Weaknesses: Stubbornness, emotional detachment, rebellious nature.");
        hashMap_zodiac_traits.put("Pisces ( मीन )","✧Characteristics: Compassionate, imaginative, intuitive.\n" +
                "✧Personality Traits: Empathetic, artistic, gentle.\n" +
                "✧Strengths: Compassion, creativity, intuition.\n" +
                "✧Weaknesses: Overly emotional, prone to escapism, indecisiveness.");
        traits=hashMap_zodiac_traits.get(Zodiac);
        Log.e( "zzzzzzzzzzzzzzzzzzz",language+"");
        if (language==0){
            Zodiac_traits_birthdate.setText(traits);
        }else {
            convert_traits();
        }
    }
    private void convert_traits(){
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(traits+" convert this in "+language_name)
                .build();


        ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                Log.e( "9999999999999 ", result.getText());
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Zodiac_traits_birthdate.setText(resultText);
                Log.e( "9999999999999 ", resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }
    private void Zodiac_finder(int day,int month){
        if (month == 12){
            if (day < 22)
                Zodiac = "Sagittarius ( धनु )";
            else
                Zodiac ="Capricorn ( मकर )";
        }

        else if (month == 1){
            if (day < 20)
                Zodiac = "Capricorn ( मकर )";
            else
                Zodiac = "Aquarius ( कुंभ )";
        }

        else if (month == 2){
            if (day < 19)
                Zodiac = "Aquarius ( कुंभ )";
            else
                Zodiac = "Pisces ( मीन )";
        }

        else if(month == 3){
            if (day < 21)
                Zodiac = "Pisces ( मीन )";
            else
                Zodiac = "Aries ( मेष )";
        }
        else if (month == 4){
            if (day < 20)
                Zodiac = "Aries ( मेष )";
            else
                Zodiac = "Taurus ( वॄष )";
        }

        else if (month == 5){
            if (day < 21)
                Zodiac = "Taurus ( वॄष )";
            else
                Zodiac = "Gemini ( मिथुन )";
        }

        else if( month == 6){
            if (day < 21)
                Zodiac = "Gemini ( मिथुन )";
            else
                Zodiac = "Cancer ( कैंसर )";
        }

        else if (month == 7){
            if (day < 23)
                Zodiac = "Cancer ( कैंसर )";
            else
                Zodiac = "Leo ( सिंह )";
        }

        else if( month == 8){
            if (day < 23)
                Zodiac = "Leo ( सिंह )";
            else
                Zodiac = "Virgo ( कन्या )";
        }

        else if (month == 9){
            if (day < 23)
                Zodiac = "Virgo ( कन्या )";
            else
                Zodiac = "Libra ( तुला )";
        }

        else if (month == 10){
            if (day < 23)
                Zodiac = "Libra ( तुला )";
            else
                Zodiac = "Scorpio ( वृश्चिक )";
        }

        else if (month == 11){
            if (day < 22)
                Zodiac = "Scorpio ( वृश्चिक )";
            else
                Zodiac = "Sagittarius ( धनु )";
        }
    }

    @SuppressLint("SetTextI18n")
    private void Birthday_Rarity() {
        Birthday_birthdate.setText(birth_date+"/"+birth_month+"/"+birth_year);
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Tell me some data and stats about how rare is birthday with date "+birth_date+" and moth "+birth_month+" in "+language_name)
                .build();


        ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                Log.e( "1111111111 ", result.getText());
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Birthday_Rarity_birthdate.setText(resultText);
                Log.e( "1111111111 ", resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }
    private void Hidden_Zodiac_Facts() {
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Tell me some Hidden Zodiac Facts about "+Zodiac+" in "+language_name)
                .build();


        ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Log.e( "7777777777 ", resultText);
                Hidden_Zodiac_Facts_birthdate.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }
    private void Day_specific_facts() {
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Tell me some Day-Specific Facts with date "+birth_date+" and moth "+birth_month+" in "+language_name)
                .build();


        ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                Log.e( "666666666 ", result.getText());
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Log.e( "666666666 ", resultText);
                Day_specific_facts_birthdate.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }
    private void Notable_Births() {
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Tell me some Notable Births with date "+birth_date+" and moth "+birth_month+" in "+language_name)
                .build();


        ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Log.e( "2222222222222", resultText);
                Notable_Births_birthdate.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }
    private void Notable_Deaths() {
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Tell me some Notable Deaths with date "+birth_date+" and moth "+birth_month+" in "+language_name)
                .build();


        ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Log.e( "3333333333 ", resultText);
                Notable_Deaths_birthdate.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }
    private void Historical_Events() {
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Tell me Historical Events with date "+birth_date+" and moth "+birth_month+" in "+language_name)
                .build();


        ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                Log.e( "44444444444 ", result.getText());
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Log.e( "44444444444 ", resultText);
                Historical_Events_birthdate.setText(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }
    public void name_meaning(){
        Name_data.setText(name);
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-2.0-flash",getEncryptedKey());
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText("Tell me some interesting facts and the meaning of name "+name+" in "+language_name)
                .build();


                ListenableFuture <GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                Log.e( "555555555555 ", result.getText());
                String resultText = Objects.requireNonNull(result.getText()).replace("**","").replace("* ","✧");
                Name_birth_data.setText(resultText);

                Log.e( "555555555555 ", resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Birth_data.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "onFailure: ", t.toString());
            }
        }, this.getMainExecutor());
    }
}