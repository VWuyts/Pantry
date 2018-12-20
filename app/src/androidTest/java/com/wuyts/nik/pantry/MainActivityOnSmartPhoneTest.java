package com.wuyts.nik.pantry;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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
    public void clickOnItemInRecyclerViewIntent() {
        onView(withId(R.id.rv_pantry_items)).check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_ITEM, click()));
        intended(hasExtraWithKey(MainActivity.ITEM_ID_KEY));
        intended(hasComponent(DetailActivity.class.getName()));
    }

    @Test
    public void clickOnItemInRecyclerView() {
        onView(withId(R.id.rv_pantry_items))
                .perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_ITEM, click()));
        onView(withId(R.id.frame_detail)).check(matches(isDisplayed()));
    }

    @Test
    public void clickFabIntent() {
        onView(withId(R.id.fab_main)).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(AddItemActivity.class.getName()));
    }

    @Test
    public void clickFab() {
        onView(withId(R.id.fab_main)).perform(click());
        onView(withId(R.id.frame_add_item)).check(matches(isDisplayed()));
    }
}
