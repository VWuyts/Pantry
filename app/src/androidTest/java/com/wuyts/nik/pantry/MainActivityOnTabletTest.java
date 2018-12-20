package com.wuyts.nik.pantry;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityOnTabletTest {
    private static final int FIRST_ITEM = 0;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkToolbarTitle() {
        CharSequence title = InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.title_main_activity);
        TestUtils.matchToolbarTitle(title).check(matches(isDisplayed()));
    }

    @Test
    public void mainFragmentIsDisplayed() {
        onView(withId(R.id.fr_main)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnItemInRecyclerView() {
        onView(withId(R.id.rv_pantry_items)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_ITEM, click()));
        onView(withId(R.id.tv_item_name)).check(matches(isDisplayed()));
    }

    @Test
    public void clickFab() {
        onView(withId(R.id.fab_main)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.tv_label_add_name)).check(matches(isDisplayed()));
    }
}
