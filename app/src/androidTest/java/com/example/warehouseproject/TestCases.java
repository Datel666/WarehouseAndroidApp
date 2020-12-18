package com.example.warehouseproject;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.warehouseproject.Code.MainActivity;

import org.junit.Rule;
import org.junit.Test;


import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class TestCases {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule = new
            ActivityScenarioRule<>(
            MainActivity.class);

    @Test
    public void NavBarIsDisplayedTest()
    {
        Espresso.onView(ViewMatchers.withId(R.id.nav_home))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.nav_itemlist))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.nav_add))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.nav_search))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.nav_history))
                .check(matches(isDisplayed()));
    }
    @Test
    public void NavBarNavigationTest()
    {
        Espresso.onView(ViewMatchers.withId(R.id.nav_itemlist))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.itemlistLabel))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.historyLabel))
                .check(doesNotExist());

        Espresso.onView(ViewMatchers.withId(R.id.nav_add))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.newitemLabel))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.historyLabel))
                .check(doesNotExist());

        Espresso.onView(ViewMatchers.withId(R.id.nav_search))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.searchLabel))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.historyLabel))
                .check(doesNotExist());

        Espresso.onView(ViewMatchers.withId(R.id.nav_history))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.historyLabel))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.searchLabel))
                .check(doesNotExist());
    }

    @Test
    public void NavdeepItemListNavigationTest(){
        Espresso.onView(ViewMatchers.withId(R.id.nav_itemlist))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.itemlistLabel))
                .check(matches(isDisplayed()));
        Espresso.onData(anything()).inAdapterView(withId(R.id.itemsGridView)).atPosition(0).
                onChildView(withId(R.id.gridImage)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.itemstypeTextView))
                .check(matches(isDisplayed()));

        Espresso.onData(anything()).inAdapterView(withId(R.id.ehgridview)).atPosition(0).
                onChildView(withId(R.id.itemtypeContainer)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.chosenitemLabel))
                .check(matches(isDisplayed()));
    }

    @Test
    public void NavAddNewItemTest()
    {
        Espresso.onView(ViewMatchers.withId(R.id.nav_add))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.newitemLabel))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.itemName))
                .perform(ViewActions.typeText("Intel celeron"));
        Espresso.onView(ViewMatchers.withId(R.id.itemName))
                .check(matches(withText("Intel celeron")));

        Espresso.onView(ViewMatchers.withId(R.id.itemCount))
                .perform(ViewActions.typeText("100"));
        Espresso.onView(ViewMatchers.withId(R.id.itemCount))
                .check(matches(withText("100")));

        Espresso.onView(ViewMatchers.withId(R.id.itemVendor))
                .perform(ViewActions.typeText("Intel"));
        Espresso.onView(ViewMatchers.withId(R.id.itemVendor))
                .check(matches(withText("Intel")));

        Espresso.onView(ViewMatchers.withId(R.id.itemDescription))
                .perform(ViewActions.typeText("Intel;Celeron;1155;2;2000"));
        Espresso.onView(ViewMatchers.withId(R.id.itemDescription))
                .check(matches(withText("Производитель:Intel;Семейство процессоров:Celeron;Сокет:1155;Количество ядер:2;Год выхода:2000")));
    }

    @Test
    public void deepNavSearchTest()
    {
        Espresso.onView(ViewMatchers.withId(R.id.nav_search))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.searchLabel))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.searchsearchEdit))
                .perform(ViewActions.typeText("Intel"));
        Espresso.onData(anything()).inAdapterView(withId(R.id.searchehGrid)).atPosition(0).
                onChildView(withId(R.id.itemtypeContainer)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.chosenitemLabel))
                .check(matches(isDisplayed()));
    }

    @Test
    public void NavHistoryTest(){
        Espresso.onView(ViewMatchers.withId(R.id.nav_history))
                .perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.historyLabel))
                .check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.historySeachField))
                .perform(ViewActions.typeText("Intel"));
    }
}
