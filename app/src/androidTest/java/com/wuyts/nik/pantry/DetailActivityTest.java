package com.wuyts.nik.pantry;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {
    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule = new ActivityTestRule<>(DetailActivity.class);

    @Test
    public void checkToolbarTitle() {
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.title_detail_activity);
        TestUtils.matchToolbarTitle(title).check(matches(isDisplayed()));
    }

    @Test
    public void detailFragmentIsDisplayed() {
        onView(withId(R.id.frame_detail)).check(matches(isDisplayed()));
    }


}
