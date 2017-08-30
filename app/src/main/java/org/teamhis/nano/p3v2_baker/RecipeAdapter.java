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

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Adapter class to dynamically provide data for the recipe list RecyclerList
 * Created by Bill on 8/10/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Recipes mRecipes;
    private Context mContext;

    public RecipeAdapter(Context context, Recipes recipes){
        this.mRecipes = recipes;
        this.mContext = context;
    }
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipe_list_content,parent,false);
        return new RecipeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecipeViewHolder holder, final int position){
       holder.recipeNameView.setText(mRecipes.getRecipe(position).getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass selected recipe in JSON format as string
                Gson gson = new Gson();
                String recipeJson = gson.toJson(mRecipes.getRecipe(position));
                Context context = v.getContext();
                // Notify Widget of update
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context,IngredientWidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.list_view);
                Intent intent = new Intent(context, StepListActivity.class);
                intent.putExtra(StepListActivity.RECIPE_JSON_KEY, recipeJson);
                context.startActivity(intent);
                }
            });
    }
    @Override
    public int getItemCount(){
        return mRecipes.size();
    }

   class RecipeViewHolder extends RecyclerView.ViewHolder {
       TextView recipeNameView;
       public final View mView;

       public RecipeViewHolder(View itemView){
           super(itemView);
           mView = itemView;
           recipeNameView = (TextView) itemView.findViewById(R.id.recipe_name);
       }
   }
}
