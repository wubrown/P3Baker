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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter class to dynamically provide data for the recipe step list RecyclerList
 * Created by Bill on 8/10/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private Context mContext;
    private Recipes.Recipe mRecipe;
    private ArrayList<String> mStepList;
    private String mLongIngredientList;

    public StepAdapter(Context context,Recipes.Recipe recipe){
        this.mContext = context;
        this.mRecipe = recipe;
        // Build modified Step decription list beginning with ingredients link for first entry
        mStepList = new ArrayList<String>();
        mStepList.add("Ingredient List");
        for(Recipes.Recipe.Step step : recipe.getSteps()){
            mStepList.add(step.getShortDescription());
        }
        mLongIngredientList = "";
        for(Recipes.Recipe.Ingredient ingredient : recipe.getIngredients()){
           mLongIngredientList = mLongIngredientList + ingredient.toString() + "\n";
        }
    }
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.step_list_content,parent,false);
        return new StepViewHolder(view);
    }
    @Override
    public void onBindViewHolder(StepViewHolder holder, final int position){
        holder.stepNameView.setText(mStepList.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String extraDescription;
                String extraVideoUrl;
                if (position == 0) {
                    extraDescription = mLongIngredientList;
                    extraVideoUrl = "";
                } else {
                    extraDescription = mRecipe.getStepDescription(position - 1);
                    extraVideoUrl = mRecipe.getStepVideoUrl(position - 1);
                }
                if (Utility.isTablet()) {
                    Bundle arguments = new Bundle();
                    arguments.putString(StepDetailFragment.STEP_DESCRIPTION, extraDescription);
                    arguments.putString(StepDetailFragment.STEP_VIDEO, extraVideoUrl);
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    FragmentManager fm = ((StepListActivity) mContext).getSupportFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.step_detail_container,fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(StepDetailActivity.STEP_DESCRIPTION, extraDescription);
                    intent.putExtra(StepDetailActivity.STEP_VIDEO, extraVideoUrl);
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount(){
        return mStepList.size();
    }
    class StepViewHolder extends RecyclerView.ViewHolder {
        TextView stepNameView;
        public final View mView;

        public StepViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            stepNameView = (TextView) itemView.findViewById(R.id.step_name);
        }
    }
}
