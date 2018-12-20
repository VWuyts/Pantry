package com.wuyts.nik.pantry;

import android.content.Intent;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasFlag;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AddItemActivityTest {

    @Rule
    public final IntentsTestRule<AddItemActivity> mActivityTestRule = new IntentsTestRule<>(AddItemActivity.class);

    @Test
    public void checkToolbarTitle() {
        CharSequence title = InstrumentationRegistry.getInstrumentation().getTargetContext().getString(R.string.title_add_activity);
        TestUtils.matchToolbarTitle(title).check(matches(isDisplayed()));
    }

    @Test
    public void addItemFragmentIsDisplayed() {
        onView(withId(R.id.frame_add_item)).check(matches(isDisplayed()));
    }

    @Test
    public void toastIsDisplayed() {
        onView(withId(R.id.et_add_name)).perform(clearText()).perform(closeSoftKeyboard());
        onView(withId(R.id.btn_add_done)).perform(click());
        onView(withText(R.string.required_name)).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void clickCancelGoToMainActivity() {
        onView(withId(R.id.btn_add_cancel)).check(matches(isDisplayed())).perform(click());
        intended(hasFlag(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        intended(hasComponent(MainActivity.class.getName()));
    }
}
