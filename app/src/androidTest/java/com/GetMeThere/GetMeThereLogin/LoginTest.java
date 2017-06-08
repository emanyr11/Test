package com.GetMeThere.GetMeThereLogin;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginTest {


    private String mEmail;
    private String mPassword;


    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initVariables(){

        mEmail = "emanyr11@gmail.com";
        mPassword = "eman123";
    }

    @Test
    public void testLogin(){

        onView(withId(R.id.et_email)).perform(typeText(mEmail));

        closeSoftKeyboard();

        onView(withId(R.id.et_password)).perform(typeText(mPassword));

        closeSoftKeyboard();

         onView(withId(R.id.btn_login)).perform(click());

    }


}
