package org.wit.twopane;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.wit.twopane.Fragment_1;
import org.wit.twopane.Fragment_2;
import org.wit.twopane.R;

public class MainActivity extends AppCompatActivity
{
    public static final int PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
    public static final int LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;

    ActionBar actionBar;
    FragmentManager fragmentManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

        actionBar = getSupportActionBar();

        fragmentManager = getSupportFragmentManager();

        // In the case of portrait any of three fragments may be attached to single activity
        if (screenOrientation() == PORTRAIT) {
            Fragment fragment = fragmentManager.findFragmentById(R.id.detailFragmentContainer);
            if (fragment == null) {
                fragment = new Fragment_1();
                fragmentManager.beginTransaction().add(R.id.detailFragmentContainer, fragment).commit();
            }

            Log.d("Twopane", "Orientation: " + screenOrientation());
        } else { // Orientation is landscape
            // In this mode a 2-pane master-detail screen is presented.
            // Fragment_main is attached to the left pane.
            // Any of the three fragments may be attached to the right pane.
            // It would be simple to prevent Fragment_main attaching to right pane.
            // See: http://stackoverflow.com/questions/10692755/how-do-i-hide-a-menu-item-in-the-actionbar
            Fragment fragment = fragmentManager.findFragmentById(R.id.detailFragmentContainer);
            if (fragment == null) {
                fragment = new Fragment_1();
                fragmentManager.beginTransaction()
                        .add(R.id.detailFragmentContainer, fragment)
                        .commit();
            }

            fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
            if(fragment == null) {
                fragment = new Fragment_main();
                fragmentManager.beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .commit();
            }
            Log.d("Twopane", "Orientation: " + screenOrientation());
        }
    }


    private void swapFragments(Fragment replacementFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack (back button will work in addition to menu).
        transaction.replace(R.id.detailFragmentContainer, replacementFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_twopane, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.fragment_main:
                swapFragments(new Fragment_main());
                Log.d("Twopane", "Fragment Main attaching");
                return true;

            case R.id.fragment_1:
                swapFragments(new Fragment_1());
                Log.d("Twopane", "Fragment 1 attaching");
                return true;
            case R.id.fragment_2:
                swapFragments(new Fragment_2());
                Log.d("Twopane", "Fragment 2 attaching");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Determines screen orientation by examining screen width and height
     * If the width is less than the height then the orientation is portrait.
     *
     * @return The screen orientation, portrait (1) or landscape (2).
     */
    public int screenOrientation() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        return w < h ? Configuration.ORIENTATION_PORTRAIT : Configuration.ORIENTATION_LANDSCAPE;

    }

}