package org.teamhis.nano.p3v2_baker;


import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Test
    public void recipeListActivityTest() {
        SystemClock.sleep(1000);
        ViewInteraction textView = onView(
                allOf(withId(R.id.recipe_name), withText("Nutella Pie"),
                        isDisplayed()));
        textView.check(matches(withText("Nutella Pie")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.recipe_name), withText("Brownies"),
                            isDisplayed()));
        textView2.check(matches(withText("Brownies")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_list_recycler_view),
                        withParent(allOf(withId(R.id.activity_recipe_list),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.step_name), withText("Cut and serve."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.step_list_recycler_view),
                                        10),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Cut and serve.")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.step_name), withText("Ingredient List"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.step_list_recycler_view),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Ingredient List")));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.step_list_recycler_view),
                        withParent(allOf(withId(R.id.activity_step_list),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pressBack();

        pressBack();

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.recipe_name), withText("Brownies"),
                        isDisplayed()));
        textView6.check(matches(withText("Brownies")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
