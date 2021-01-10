package com.prince.bakeryapp;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;


public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void clickGridViewItem_OpensOrderActivity() {


        onData(anything()).inAdapterView(withId(R.id.recipe_grid_recycler_view)).atPosition(1).perform(click());


        onView(withId(R.id.recipe_title)).check(matches(withText("Brownies")));


    }
}
