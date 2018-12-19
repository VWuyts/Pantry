package com.wuyts.nik.pantry;

import android.support.test.InstrumentationRegistry;
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

@RunWith(AndroidJUnit4.class)
public class MainActivityOnSmartPhoneTest {
    private static final int FIRST_ITEM = 0;

    @Rule
    public final IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void checkToolbarTitle() {
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.title_main_activity);
        TestUtils.matchToolbarTitle(title).check(matches(isDisplayed()));
    }

    @Test
    public void mainFragmentIsDisplayed() {
        onView(withId(R.id.fr_main)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnItemInRecyclerView() {
        onView(withId(R.id.rv_pantry_items)).perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_ITEM, click()));
        intended(hasExtraWithKey(MainActivity.ITEM_ID_KEY));
        intended(hasComponent(DetailActivity.class.getName()));
    }

    @Test
    public void clickFab() {
        onView(withId(R.id.fab_main)).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(AddItemActivity.class.getName()));
    }
}
