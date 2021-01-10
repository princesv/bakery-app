package com.prince.bakeryapp;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class StepsActivityBasicTest {

    @Rule
    public ActivityTestRule<StepsActivity> mTestRule = new ActivityTestRule<>(StepsActivity.class);

    @Test
    public void clickDecrementButton_ChangesQuantityAndCost() {
        onView(withId(R.id.index_number)).check(matches(withText(1)));
        onView(withId(R.id.button_next_step)).perform(click());
        onView(withId(R.id.index_number)).check(matches(withText(2)));
    }

}
