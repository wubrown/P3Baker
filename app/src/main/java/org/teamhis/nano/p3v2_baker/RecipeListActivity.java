/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.teamhis.nano.p3v2_baker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
* This is the starting activity of an application that will import a remote collection of recipes, display the list of recipes,
* on user recipe selection, display a list of ingredients and recipe steps,
* on user selection of a step, display a detail step description and video when available.
*
* This specific activity displays a recipe selection list and sends selection details to the
* StepListActivity
 */

public class RecipeListActivity extends AppCompatActivity {

    private boolean mIsTablet;
    private Recipes mRecipes;
    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Utility.updateConfig(this);
        mIsTablet=Utility.isTablet();
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.recipe_list_toolbar);
        setSupportActionBar(toolbar);

        mRecipeRecyclerView = (RecyclerView) findViewById(R.id.recipe_list_recycler_view);
        assert mRecipeRecyclerView != null;
        if(mIsTablet){
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        downloadRecipes();

    }
    private void downloadRecipes() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/");

        Retrofit retrofit = builder.build();

        RecipiesClient client = retrofit.create(RecipiesClient.class);
        Call<ResponseBody> call = client.downloadRecipes();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // following required when the root of the JSON string is a list not an object
                    String buf = "{\"recipes\": " + response.body().string() + "}";
                   mRecipes = new Gson().fromJson(buf,Recipes.class);
                    mRecipeAdapter = new RecipeAdapter(mContext, mRecipes);
                    mRecipeRecyclerView.setAdapter(mRecipeAdapter);
                } catch (Exception e) {
                    Toast.makeText(RecipeListActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RecipeListActivity.this, "error: (", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
