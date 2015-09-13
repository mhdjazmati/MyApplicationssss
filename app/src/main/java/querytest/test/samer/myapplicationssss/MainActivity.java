package querytest.test.samer.myapplicationssss;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    public static NavigationDrawerFragment mNavigationDrawerFragment;

    private Toolbar mToolbar;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        //mToolbar.setBackgroundResource(R.drawable.toolbar11);
        //mToolbar.setBackgroundResource(R.drawable.toolbar);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer


        SharedPreferences pref = getApplicationContext().getSharedPreferences("TEST_NAME", MODE_PRIVATE);
        String coverImagePath=  pref.getString(MySettingsActivity.PREF_KEY_COVER_PIC,null);
        if(coverImagePath==null) {
            mNavigationDrawerFragment.setUserData("Mohammad Djazmati", "mhdjazmati@gmail.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));
        }
        else // there's a saved pic
        {
            BitmapFactory.Options  options =new BitmapFactory.Options();
            mNavigationDrawerFragment.setUserData("Mohammad Djazmati", "mhdjazmati@gmail.com", BitmapFactory.decodeFile(coverImagePath));
        }

        avatar=(ImageView) findViewById(R.id.imgAvatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Go to profile",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        switch (position) {
            case 0: //events//
                fragment = getFragmentManager().findFragmentById(R.id.fragment_event);
                if (fragment == null) {
                    fragment = new EventsFragment();

                }
                getFragmentManager().beginTransaction().replace(R.id.container,
                        fragment).commit();
                if(actionBar!=null)actionBar.setTitle("Events");
                break;

            case 1: //drivers
                fragment = getFragmentManager().findFragmentById(R.id.fragment_driver);
                if (fragment == null) {
                    fragment = new DriversFragment();

                }
                getFragmentManager().beginTransaction().replace(R.id.container,
                        fragment).commit();
                if(actionBar!=null)actionBar.setTitle("Drivers");
                break;
            case 2: //trucks //
                fragment = getFragmentManager().findFragmentById(R.id.fragment_truck);
                if (fragment == null) {
                    fragment = new TrucksFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container,
                        fragment).commit();
                if(actionBar!=null)actionBar.setTitle("Trucks");
                break;


            case 3: //performance //
                fragment = getFragmentManager().findFragmentById(R.id.fragment_performance);
                if (fragment == null) {
                    fragment = new PerformanceFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container,
                        fragment).commit();
                if(actionBar!=null)actionBar.setTitle("Performance");
                break;
        }

    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i =new Intent(this,MySettingsActivity.class);
            startActivity(i);
        }
        if(id==R.id.action_example)
        {
            Toast.makeText(this,"AHAAAY",Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }


}
