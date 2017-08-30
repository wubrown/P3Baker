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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

/*
* This specific activity displays a recipe step selection list and sends selection details to the
* StepDetailActivity
 */
public class StepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mIsTablet;
    public static final String RECIPE_JSON_KEY= "recipe_json";
    private Recipes.Recipe mRecipe;
    private RecyclerView mStepRecyclerView;
    private StepAdapter mStepAdapter;
    private Context mContext;

    private static final String TAG = "StepListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        mContext = this;

        Bundle extras = getIntent().getExtras();
        String recipeJson = extras.getString(RECIPE_JSON_KEY);
        Gson gson = new Gson();
        mRecipe = gson.fromJson(recipeJson, Recipes.Recipe.class);
//        Log.v(TAG,mRecipe.getName());

        mStepRecyclerView = (RecyclerView) findViewById(R.id.step_list_recycler_view);

        assert mStepRecyclerView != null;
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStepAdapter = new StepAdapter(mContext,mRecipe);
        mStepRecyclerView.setAdapter(mStepAdapter);
        mIsTablet=Utility.isTablet();
    }




}
