package ma.ensa.dictionaryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ma.ensa.dictionaryapp.model.Example;
import ma.ensa.dictionaryapp.model.Word;

public class DefinitionActivity extends AppCompatActivity {
    private TextView id;
    private TextView type;
    private TextView description;
    private TextView example;
    private TextView updesc;
    private ImageView fav;
    private Word word;
    private ArrayList<Word> favoriteList;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_definitions);
        word = (Word) getIntent().getSerializableExtra("word");

        String updesString = "Definition of "
                +word.getId()+" "+word.getDefinitions().get(0).getType()
                +" from the Oxford Advanced Learner's Dictionary";

        System.out.println(word);
        id = findViewById(R.id.nameId);
        type = findViewById(R.id.nameType);
        description = findViewById(R.id.nameDefinition1);
        example = findViewById(R.id.example1);
        updesc = findViewById(R.id.updesc);
        fav = findViewById(R.id.imageViewFav);

        id.setText(word.getId());
        type.setText(word.getDefinitions().get(0).getType());
        description.setText(word.getDefinitions().get(0).getDefinition());
        if(word.getDefinitions().get(0).getExamples() == null){
            findViewById(R.id.exampleeTitle).setVisibility(View.GONE);
            example.setVisibility(View.GONE);
        }else{
            example.setText(word.getDefinitions()
                    .get(0).getExamples()
                    .get(0).getExample());
        }
        updesc.setText(updesString);
        loadData();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<Word>>() {}.getType();
        favoriteList = gson.fromJson(json, type);
        if (favoriteList == null) {
            favoriteList = new ArrayList<>();
        }
    }
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoriteList);
        editor.putString("words", json);
        editor.apply();
        loadData();
        Toast.makeText(this, "This word is saved in favorite ", Toast.LENGTH_SHORT).show();
    }
    public void doFavorite(View view){
        System.out.println("clicked");
        fav.setImageResource(R.drawable.ic_baseline_favorite_true_24);
        word.setFavorite(true);
        favoriteList.add(word);
        saveData();
    }
}