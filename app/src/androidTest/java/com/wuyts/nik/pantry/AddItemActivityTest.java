package com.wuyts.nik.pantry;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasFlag;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AddItemActivityTest {
    @Rule
    public ActivityTestRule<AddItemActivity> mActivityTestRule = new ActivityTestRule<>(AddItemActivity.class);

    @Rule
    public IntentsTestRule<AddItemActivity> mIntentsTestRule = new IntentsTestRule<>(AddItemActivity.class);

    @Test
    public void checkToolbarTitle() {
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.title_add_activity);
        TestUtils.matchToolbarTitle(title).check(matches(isDisplayed()));
    }

    @Test
    public void detailFragmentIsDisplayed() {
        onView(withId(R.id.frame_add_item)).check(matches(isDisplayed()));
    }

    @Test
    public void clickCancelGoToMainActivity() {
        onView(withId(R.id.btn_add_cancel)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_add_cancel)).perform(click());
        intended(hasFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        intended(hasComponent(MainActivity.class.getName()));
    }
}
