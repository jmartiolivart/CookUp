package com.example.cookup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cookup.Logic.Enums.Type;
import com.example.cookup.Logic.Ingredient;
import com.example.cookup.Logic.Preparation;
import com.example.cookup.Logic.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.SneakyThrows;

public class AdvancedSearchFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Recipe> recipes = new ArrayList<>();
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_adv_search, container, false);

        Button advSearchprepare = view.findViewById(R.id.AdvSearcherPrepare_button);
        Button advSearch = view.findViewById(R.id.AdvSearcher_button);
        advSearchprepare.setOnClickListener(this);
        advSearch.setOnClickListener(this);

        return view;
    }

    @SneakyThrows
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.AdvSearcherPrepare_button:
                EditText nameRecipe = getActivity().findViewById(R.id.nameRecipesearch);
                EditText foodtype = getActivity().findViewById(R.id.foodtypesearch);
                EditText dishtype = getActivity().findViewById(R.id.dishtypesearch);

                String name = nameRecipe.getText().toString();
                String food = foodtype.getText().toString();
                String dish = dishtype.getText().toString();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                if(name.isEmpty() && food.isEmpty() && dish.isEmpty()){
                    db.collection("recipes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    createRecipefromDocument(document);
                                }
                            }
                        }
                    });
                }else if(food.isEmpty() && dish.isEmpty()){
                    db.collection("recipes").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    createRecipefromDocument(document);
                                }
                            }
                        }
                    });
                }else if(name.isEmpty() && dish.isEmpty()){
                    db.collection("recipes").whereEqualTo("foodtype", food).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    createRecipefromDocument(document);
                                }
                            }
                        }
                    });
                }else if(name.isEmpty() && food.isEmpty()){
                    db.collection("recipes").whereEqualTo("dishtype", dish).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    createRecipefromDocument(document);
                                }
                            }
                        }
                    });
                }else if(name.isEmpty()){
                    db.collection("recipes").whereEqualTo("foodtype", food).whereEqualTo("dishtype", dish).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    createRecipefromDocument(document);
                                }
                            }
                        }
                    });
                }else if(food.isEmpty()){
                    db.collection("recipes").whereEqualTo("name", name).whereEqualTo("dishtype", dish).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    createRecipefromDocument(document);
                                }
                            }
                        }
                    });
                }else if(dish.isEmpty()){
                    db.collection("recipes").whereEqualTo("name", name).whereEqualTo("foodtype", food).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    createRecipefromDocument(document);
                                }
                            }
                        }
                    });
                }else{
                    db.collection("recipes").whereEqualTo("name", name).whereEqualTo("foodtype", food).whereEqualTo("dishtype", dish).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    createRecipefromDocument(document);
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.AdvSearcher_button:
                Intent mainintent1 = new Intent(getActivity(), AdvancedSearchResultActivity.class);
                mainintent1.putExtra("recipes", recipes);
                startActivity(mainintent1);
        }
    }


    public void createRecipefromDocument(QueryDocumentSnapshot document){
        Recipe recipe = new Recipe(document.get("name").toString());
        recipe.setDishtype(document.get("dishtype").toString());
        recipe.setFoodtype(document.get("foodtype").toString());
        recipe.setDescription(document.get("description").toString());
        recipe.setServings(Integer.parseInt(document.get("servings").toString()));
        ArrayList<Ingredient> ingrlist = new ArrayList<>();
        ArrayList<Preparation> preplist = new ArrayList<>();
        ArrayList<Map<String, Object>> ingr = (ArrayList<Map<String, Object>>) document.get("ingredients");
        ArrayList<Map<String, Object>> prep = (ArrayList<Map<String, Object>>) document.get("preparations");
        for(int i = 0; i < ingr.size(); i++){
            Ingredient ingredient = new Ingredient(ingr.get(i).get("ingredient").toString(), Integer.parseInt(ingr.get(i).get("amount").toString()),setType(ingr.get(i).get("type").toString()));
            ingrlist.add(ingredient);
        }
        for(int i = 0; i < prep.size(); i++){
            Preparation preparation = new Preparation(prep.get(i).get("pass").toString());
            preplist.add(preparation);
        }
        recipe.setIngredients(ingrlist);
        recipe.setPreparations(preplist);
        recipes.add(recipe);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.config:
                startActivity(new Intent(getActivity(), com.example.cookup.preferences.PreferencesActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
