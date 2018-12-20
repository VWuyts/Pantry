package com.wuyts.nik.pantry;

import android.content.Context;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityOnSmartPhoneTest {
    private static final int FIRST_ITEM = 0;

    @Rule
    public final IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void checkToolbarTitle() {
        CharSequence title = getApplicationContext().getString(R.string.title_main_activity);
        TestUtils.matchToolbarTitle(title).check(matches(isDisplayed()));
    }

    @Test
    public void mainFragmentIsDisplayed() {
        onView(withId(R.id.fr_main)).check(matches(isDisplayed()));
    }

    @Test
    public void dynamicFragmentIsNotDisplayed() {
        onView(withId(R.id.fr_dynamic)).check(doesNotExist());
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

    // Verify that the first item in the RecyclerView corresponds to R.string.item_canned_t_cubes
    // before running this test
    @Test
    public void swipeItemInRecyclerView() {
        onView(withId(R.id.rv_pantry_items))
                .perform(RecyclerViewActions.actionOnItemAtPosition(FIRST_ITEM, swipeLeft()));
        onView(withText(R.string.item_canned_t_cubes)).check(matches(isDisplayed()));
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

    // Verify that an item to be bought at 'Colruyt' is present in the list before running this test
    @Test
    public void clickItemInOverflowMenuIntent() {
        Context context = getApplicationContext();
        String menuItem = context.getString(R.string.go_to_shop) + " " + context.getString(R.string.shop_colruyt);
        String shop = context.getString(R.string.shop_colruyt);

        openActionBarOverflowOrOptionsMenu(getApplicationContext());
        onView(withText(menuItem)).perform(click());
        intended(hasExtra(ShopActivity.SHOP_KEY, shop));
        intended(hasComponent(ShopActivity.class.getName()));
    }

    // Verify that an item to be bought at 'Colruyt' is present in the list before running this test
    @Test
    public void clickItemInOverflowMenu() {
        Context context = getApplicationContext();
        CharSequence title = context.getString(R.string.shop_colruyt);
        String menuItem = context.getString(R.string.go_to_shop) + " " + context.getString(R.string.shop_colruyt);

        openActionBarOverflowOrOptionsMenu(getApplicationContext());
        onView(withText(menuItem)).perform(click());
        TestUtils.matchToolbarTitle(title).check(matches(isDisplayed()));
    }
}
