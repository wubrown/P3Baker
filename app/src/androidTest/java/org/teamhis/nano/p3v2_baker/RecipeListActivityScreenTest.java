package org.teamhis.nano.p3v2_baker;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Bill on 8/22/2017.
 */
@RunWith(AndroidJUnit4.class)

public class RecipeListActivityScreenTest {
    public static final String RECIPE_NAME = "Brownies";
    @Rule
    public IntentsTestRule<RecipeListActivity> mActivityTestRule = new IntentsTestRule<>(RecipeListActivity.class);

    @Test
    public  void recipeListDisplayedOnStart() {
        onView(withId(R.id.recipe_list_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void stepListDisplayedOnRecipeClick() {
       onView(withId(R.id.recipe_list_recycler_view))
               .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.step_list_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecipeCreatesIntent(){
        onView(withId(R.id.recipe_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        intended(allOf(
                hasComponent(StepListActivity.class.getName()),hasExtraWithKey(StepListActivity.RECIPE_JSON_KEY)));
    }
}
