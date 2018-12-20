package com.wuyts.nik.pantry;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.appcompat.widget.Toolbar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static org.hamcrest.core.Is.is;

final class TestUtils {

    // Ref: http://blog.sqisland.com/2015/05/espresso-match-toolbar-title.html
    public static ViewInteraction matchToolbarTitle(CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class)).check(matches(withToolbarTitle(is(title))));
    }

    /* Custom matcher */
    // Ref: http://blog.sqisland.com/2015/05/espresso-match-toolbar-title.html
    private static Matcher<Object> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {

            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    } // end matcher withToolbarTitle
}
