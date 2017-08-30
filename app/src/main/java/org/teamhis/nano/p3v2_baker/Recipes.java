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

import java.util.ArrayList;

/**
 * Created by Bill on 7/28/2017.
 * This class provides the model for recipe information as converted from the JSON resipe file
 */

public class Recipes {
    private ArrayList<Recipe> recipes;
    private static String sCurrentRecipeName = "";
    private static ArrayList<String> sCurrentIngredientList = null;
    public Recipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
    public void addRecipe(Recipe recipe){
        recipes.add(recipe);
    }
    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }
    // Note: logs name of last recipe selected for potential use in widget
    public Recipe getRecipe(int position){
        Recipe recipe = recipes.get(position);
        sCurrentRecipeName=recipe.getName();
        sCurrentIngredientList=new ArrayList<String>();
        for (Recipe.Ingredient i : recipe.getIngredients()){
            sCurrentIngredientList.add(i.toString());
        }

        return recipe;
    }
    public int size(){
        return recipes.size();
    }
    public static String getCurrentRecipeName(){
        return sCurrentRecipeName;
    }
    public static ArrayList<String> getCurrentIngredientList(){
        return sCurrentIngredientList;
    }
    public class Recipe {
        private int id;
        private String name;
        private ArrayList<Ingredient> ingredients;
        private ArrayList<Step> steps;
        private int servings;

        public Recipe(int id, String name, ArrayList<Ingredient> ingredients,
                      ArrayList<Step> steps, int servings){
            this.id = id;
            this.name = name;
            this.ingredients = ingredients;
            this.steps = steps;
            this.servings = servings;
        }
        public int getId(){
            return id;
        }
        public String getName() {
            return name;
        }
        public ArrayList<Ingredient> getIngredients(){
            return ingredients;
        }
        public ArrayList<Step> getSteps(){
            return steps;
        }
        public int getServings(){
            return servings;
        }
        public String getStepDescription(int index){
            return steps.get(index).description;
        }
        public String getStepVideoUrl(int index){
            return steps.get(index).videoURL;
        }
        public Ingredient getIngredient(int position){
            return ingredients.get(position);
        }
        public class Ingredient {
            private double quantity;
            private String measure;
            private String ingredient;

            public Ingredient(double quantity, String measure, String ingredient){
                this.quantity= quantity;
                this.measure= measure;
                this.ingredient=ingredient;
            }
            public String toString(){
                return "" + quantity+" "+measure+" "+ingredient;
            }
        }

        public class Step {
            private int id;
            private String shortDescription;
            private String description;
            private String videoURL;
            private String thumbnailURL;

            public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
                this.id = id;
                this.shortDescription = shortDescription;
                this.description = description;
                this.videoURL = videoURL;
                this.thumbnailURL = thumbnailURL;
            }
            public String getShortDescription(){
                return shortDescription;
            }
        }
    }
}
