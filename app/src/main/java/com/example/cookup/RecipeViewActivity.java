package com.example.cookup;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookup.Logic.Ingredient;
import com.example.cookup.Logic.Preparation;
import com.example.cookup.Logic.Recipe;

import java.util.ArrayList;

public class RecipeViewActivity extends AppCompatActivity {

    private ArrayList<String> ingr = new ArrayList<>();
    private ArrayList<String> prep = new ArrayList<>();

    ListView Ingredients;
    ListView Preparations;

    ArrayAdapter<String> ingradapter;
    ArrayAdapter<String> prepadapter;

    ArrayList<Ingredient> ingrlist = new ArrayList<>();
    ArrayList<Preparation> preplist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        Ingredients = findViewById(R.id.IngredientListView);
        Preparations = findViewById(R.id.PreparationListView);

        TextView name = findViewById(R.id.RecipeNameView);
        TextView serv = findViewById(R.id.RecipeServingView);
        TextView dish = findViewById(R.id.RecipeDishView);
        TextView food = findViewById(R.id.RecipeFoodView);
        TextView desc = findViewById(R.id.RecipeDescriptionView);

        ingrlist = recipe.getIngredients();
        preplist = recipe.getPreparations();


        for(int i = 0; i < ingrlist.size(); i++){
            Ingredient ingredient = new Ingredient(ingrlist.get(i).getIngredient(),ingrlist.get(i).getAmount(),ingrlist.get(i).getType());
            ingr.add(ingredient.toString());
        }

        for(int i = 0; i < preplist.size(); i++){
            Preparation preparation = new Preparation(preplist.get(i).getPass());
            prep.add(preparation.toString());
        }


        name.setText(recipe.getName());
        serv.setText(String.valueOf(recipe.getServings()));
        dish.setText(recipe.getDishtype());
        food.setText(recipe.getFoodtype());
        desc.setText(recipe.getDescription());


        LoadAdapterIngredients();
        LoadAdapterPreparations();
    }

    public void LoadAdapterIngredients(){
        ingradapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , ingr);
        Ingredients.setAdapter(ingradapter);
    }

    public void LoadAdapterPreparations(){
        prepadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, prep);
        Preparations.setAdapter(prepadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.config:
                startActivity(new Intent(this, com.example.cookup.preferences.PreferencesActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
