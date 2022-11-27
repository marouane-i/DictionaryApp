package ma.ensa.dictionaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ma.ensa.dictionaryapp.model.Example;
import ma.ensa.dictionaryapp.model.Word;

public class DefinitionActivity extends AppCompatActivity {
    private TextView id;
    private TextView type;
    private TextView description;
    private TextView example;
    private TextView updesc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definitions);
        Word word = (Word) getIntent().getSerializableExtra("word");

        String updesString = "Definition of "
                +word.getId()+" "+word.getDefinitions().get(0).getType()
                +" from the Oxford Advanced Learner's Dictionary";

        System.out.println(word);
        id = findViewById(R.id.nameId);
        type = findViewById(R.id.nameType);
        description = findViewById(R.id.nameDefinition1);
        example = findViewById(R.id.example1);
        updesc = findViewById(R.id.updesc);

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
    }
}