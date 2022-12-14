package ma.ensa.dictionaryapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.net.ssl.HttpsURLConnection;

import ma.ensa.dictionaryapp.model.Word;
import ma.ensa.dictionaryapp.model.Definition;
import ma.ensa.dictionaryapp.model.Example;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Word> worldList;

    private String mParam1;
    private String mParam2;

    private LoadingDialog loadingDialog;

    public HomeFragment() {
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        loadingDialog = new LoadingDialog(getActivity());
        Button button = (Button) view.findViewById(R.id.buttonSearch);
        button.setOnClickListener(new View.OnClickListener() {
        EditText editText = view.findViewById(R.id.searchWord);
            public void onClick(View v) {
                String searchWord = editText.getText().toString();
                if(searchWord.isEmpty())
                    Toast.makeText(v.getContext(), "Enter a word please", Toast.LENGTH_LONG).show();
                else{
                    Word foundWord = null;
                    for(Word item:worldList){
                        if(item.getId().equals(searchWord)){
                            foundWord=item;
                        }
                    }
                    if(foundWord!=null){
                        Intent intent = new Intent(getContext(), DefinitionActivity.class);
                        intent.putExtra("word", foundWord);
                        startActivity(intent);
                    }else {
                        loadingDialog.startLoadingDialog();
                        CallbackTask task = new CallbackTask();
                        task.execute(dictionaryEntries(searchWord));
                    }
                }

            }
        });
    }

    private String dictionaryEntries(String word) {
        final String language = "en-us";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
    }

    public void onResponse(String result){
        try {
            Word word = new Word();
            JSONObject root = new JSONObject(result);
            JSONArray results = root
                    .getJSONArray("results");
            String id = root.getString("id");
            String audioFile = results.getJSONObject(0)
                    .getJSONArray("lexicalEntries")
                    .getJSONObject(0)
                    .getJSONArray("entries")
                    .getJSONObject(0)
                    .getJSONArray("pronunciations")
                    .getJSONObject(1)
                    .getString("audioFile");
            List<Definition> definitions = new ArrayList<>();

            for(int i=0; i<results.length(); i++){
                JSONObject senses = results.getJSONObject(i)
                        .getJSONArray("lexicalEntries")
                        .getJSONObject(0)
                        .getJSONArray("entries")
                        .getJSONObject(0)
                        .getJSONArray("senses")
                        .getJSONObject(0);
                String type = results.getJSONObject(i)
                        .getJSONArray("lexicalEntries")
                        .getJSONObject(0)
                        .getJSONObject("lexicalCategory")
                        .getString("id");

                String definitionText = senses
                        .getJSONArray("definitions")
                        .getString(0);
                Definition definitionObject = new Definition();
                definitionObject.setDefinition(definitionText);
                definitionObject.setType(type);

                if(senses.has("examples")){
                    JSONArray examplesArray = senses
                            .getJSONArray("examples");
                    List<Example> examples = new ArrayList<>();
                    for(int j=0; j<examplesArray.length(); j++){
                        String example = examplesArray
                                .getJSONObject(j)
                                .getString("text");
                        examples.add(new Example(example));
                    }
                    definitionObject.setExamples(examples);
                }
                definitions.add(definitionObject);
            }
            word.setId(id);
            word.setAudioFile(audioFile);
            word.setDefinitions(definitions);
            Intent intent = new Intent(getContext(), DefinitionActivity.class);
            intent.putExtra("word", word);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared_preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("words", null);
        Type type = new TypeToken<ArrayList<Word>>() {}.getType();
        worldList = gson.fromJson(json, type);
        if (worldList == null) {
            worldList = new ArrayList<>();
        }
    }

    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String[] params) {
            final String app_id = "08a9321a";
            final String app_key = "d745196922723fb783ce8b0fa103f424";
            try {
                URL url = new URL(params[0].toString());
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
            }catch (Exception e){
                Handler handler = new Handler(getActivity().getMainLooper());
                handler.post(()->Toast.makeText(getActivity().getBaseContext(), "invalid word", Toast.LENGTH_LONG).show());
                e.printStackTrace();
                return e.toString();
            }finally {
                loadingDialog.dismissDialog();
            }
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            onResponse(o);
        }
    }
}