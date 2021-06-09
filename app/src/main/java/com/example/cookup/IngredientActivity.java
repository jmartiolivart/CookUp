package com.example.cookup;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cookup.Logic.Enums.Type;
import com.example.cookup.Logic.Ingredient;

public class IngredientActivity extends AppCompatActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addingredient);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button createIngredient = findViewById(R.id.createIngredientButton);

        createIngredient.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Crear Ingrediente i devolver el ingrediente creado al startActivityForResult
        EditText nameIngredient = findViewById(R.id.nameIngredient);
        EditText amountText = findViewById(R.id.amount);
        Spinner amountType = findViewById(R.id.spinnerType);

        String name = nameIngredient.getText().toString();
        int amount = Integer.parseInt(amountText.getText().toString());
        String type = amountType.getSelectedItem().toString();

        Type ingredientType = setType(type);

        Ingredient ingredient = new Ingredient(name,amount,ingredientType);

        Intent intent = new Intent();
        intent.putExtra("ingredient", ingredient);
        setResult(RESULT_OK, intent);
        finish();
    }

    public Type setType(String type){
        if(type.equals(Type.cuchara_sopera.toString())){
            return Type.cuchara_sopera;
        }else if(type.equals(Type.cucharilla.toString())){
            return Type.cucharilla;
        }else if(type.equals(Type.gr.toString())){
            return Type.gr;
        }else{
            return Type.ml;
        }
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
