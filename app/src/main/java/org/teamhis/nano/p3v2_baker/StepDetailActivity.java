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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

/*
* This activity displays the details of a specific recipe step
 */

public class StepDetailActivity extends AppCompatActivity {
    public static final String STEP_DESCRIPTION = "step_description";
    public static final String STEP_VIDEO = "step_video";
    public static final String STEP_IMAGE = "step_image";
    public static final String STEP_IS_INGREDIENTS = "step_is_ingredients";
    private String mStepDescription;
    private String mStepVideoUrl;
    private String mStepImage;
    private boolean mStepIsIngredients;
    private Context mContext;
    private boolean mIsPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        mContext = this;
        Utility.updateConfig(this);
        mIsPortrait = Utility.isPortrait();

        Toolbar toolbar = (Toolbar) findViewById(R.id.step_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        mStepDescription = extras.getString(STEP_DESCRIPTION);
        mStepVideoUrl = extras.getString(STEP_VIDEO);
        mStepImage = extras.getString(STEP_IMAGE);
        mStepIsIngredients = extras.getBoolean(STEP_IS_INGREDIENTS);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(StepDetailFragment.STEP_DESCRIPTION,mStepDescription);
            arguments.putString(StepDetailFragment.STEP_VIDEO,mStepVideoUrl);
            arguments.putString(StepDetailFragment.STEP_IMAGE,mStepImage);
            arguments.putBoolean(StepDetailFragment.STEP_IS_INGREDIENTS,mStepIsIngredients);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            NavUtils.navigateUpTo(this,upIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
