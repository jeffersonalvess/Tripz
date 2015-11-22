package edu.depaul.csc472.tripz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WelcomeScreen extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static final String FACEBOOK_PREFS = "FacebookPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        FacebookSdk.sdkInitialize(getApplicationContext());

        SharedPreferences settings = getSharedPreferences(FACEBOOK_PREFS, 0);

        if (settings.getBoolean("loginSucessful", false)) {
            Intent intent = new Intent();
            //intent.setClass(WelcomeScreen.this, MainActivity.class);
            intent.setClass(WelcomeScreen.this, GoogleMapsTest.class);
            startActivity(intent);
            finish();
        }


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_welcome_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        //The fragment argument representing the section number for this fragment
        private static final String ARG_SECTION_NUMBER = "section_number";

        //Facebook Login
        private CallbackManager mCallBackManager;
        private LoginButton fbLoginButton;

        //Returns a new instance of this fragment for the given section number.
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                if (profile != null) {
                    //Do something with data obtained

                }

                SharedPreferences settings = getContext().getSharedPreferences(FACEBOOK_PREFS, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("loginSucessful", true);
                editor.commit();

                Intent intent = new Intent(PlaceholderFragment.this.getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

                // System.out.println("Facebook Login Successful!");
                // System.out.println("Logged in user Details : ");
                // System.out.println("--------------------------");
                // System.out.println("User ID  : " + loginResult.getAccessToken().getUserId());
                // System.out.println("Authentication Token : " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                System.out.println("Facebook Login failed!!");

            }

            @Override
            public void onError(FacebookException e) {
                System.out.println("Facebook Login failed!!");
            }
        };

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

            //Automatic Code
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            TextView txtWelcome = (TextView) rootView.findViewById(R.id.txtWelcome);
            ImageView imgLogo = (ImageView) rootView.findViewById(R.id.imgLogo);
            ImageView imgBalls = (ImageView) rootView.findViewById(R.id.imgBalls);
            fbLoginButton = (LoginButton) rootView.findViewById(R.id.fb_login_button);

            mCallBackManager = CallbackManager.Factory.create();

            fbLoginButton.setFragment(this);
            fbLoginButton.registerCallback(mCallBackManager, mCallback);

            getFbKeyHash("edu.depaul.csc472.tripz");


            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    txtWelcome.setText("Welcome to Tripz");
                    imgLogo.setImageResource(R.drawable.tripzlogo);
                    imgBalls.setImageResource(R.drawable.ballsfirst);
                    break;
                case 2:
                    txtWelcome.setText("Tripz will help you to manage your trips easily.");
                    txtWelcome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    imgLogo.setImageResource(R.drawable.tripzlogo);
                    imgBalls.setImageResource(R.drawable.ballssecond);
                    break;
                case 3:
                    txtWelcome.setText("Enter with Facebook to start");
                    txtWelcome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    imgLogo.setImageResource(R.drawable.facebooklogo);
                    imgBalls.setImageResource(R.drawable.ballsthird);
                    fbLoginButton.setVisibility(View.VISIBLE);
                    break;
            }

            return rootView;
        }

        @Override
        public void onActivityResult(int reqCode, int resCode, Intent i) {
            super.onActivityResult(reqCode, resCode, i);
            mCallBackManager.onActivityResult(reqCode, resCode, i);
        }

        public void getFbKeyHash(String packageName) {
            try {
                PackageInfo info = getContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    System.out.println("YourKeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }

        }
    }
}