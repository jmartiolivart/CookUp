package com.example.cookup;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookup.Logic.Recipe;

import java.util.ArrayList;

public class AdvancedSearchResultActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipes = new ArrayList<>();

    private ArrayList<String> rec = new ArrayList<>();

    ListView Recipe;

    ArrayAdapter<String> recipeadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.fragment_home);

        Intent intent = getIntent();
        ArrayList<Recipe> recipesfromintent = (ArrayList<Recipe>)  intent.getSerializableExtra("recipes");

        recipes = recipesfromintent;

        Recipe = findViewById(R.id.RecipeList);
        for (Recipe recipe:  recipes) {
            rec.add(recipe.getName());
        }

        recipeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , rec);
        Recipe.setAdapter(recipeadapter);

        Recipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = recipes.get(position);

                Intent i = new Intent(AdvancedSearchResultActivity.this,RecipeViewActivity.class);
                i.putExtra("recipe", recipe);
                startActivity(i);
            }
        });


    }

}
