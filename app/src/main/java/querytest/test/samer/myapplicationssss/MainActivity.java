package querytest.test.samer.myapplicationssss;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AppKeyPair;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.GET_TASKS;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WAKE_LOCK;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {
    public static boolean RELOAD = false;
    public static final String PERMISSIONS[] = {WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE,
            INTERNET,
            VIBRATE,
            ACCESS_WIFI_STATE,
            ACCESS_NETWORK_STATE,
            WAKE_LOCK,
            GET_TASKS};

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    public static NavigationDrawerFragment mNavigationDrawerFragment;
    private static final int IMAGE_REQUEST_CODE = 3;

    private Toolbar mToolbar;
    private ImageView avatar;
    public static SharedPreferences prefs;
    public static String encodedCoverImage;
    public boolean exit;
    public static AVLoadingIndicatorView avi;

    BroadcastReceiver newDataReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Fragment fragmentById = getFragmentManager().findFragmentById(R.id.container);
            String simpleClassName = fragmentById.getClass().getSimpleName();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.getString(CheckForNewDataService.GET_ACTION, "").equals(CheckForNewDataService.BROADCAST_NEW_DATA_IMAGES)) {
                    if (simpleClassName.equals(ImagesFragment.class.getSimpleName())) {
                        int resultCode = bundle.getInt(CheckForNewDataService.RESULT);
                        if (resultCode == RESULT_OK) {
                            ArrayList<String> newThumbs = bundle.getStringArrayList(CheckForNewDataService.DATA);
                            //((BROADCAST_NEW_DATA_IMAGES) fragmentById).myAdapter.notifyDataSetChanged();
                            ImagesFragment fragmentById1 = (ImagesFragment) fragmentById;
                            ImagesFragment.imagePath = newThumbs;
                            //fragmentById1.myAdapter = new fragmentById1.MyAdapter(fragmentById1.getActivity());
                            fragmentById1.myAdapter.notifyDataSetChanged();
                            ImagesFragment.gridview.setAdapter(fragmentById1.myAdapter);
                        }
                    }
                } else if (bundle.getString(CheckForNewDataService.GET_ACTION, "").equals(CheckForNewDataService.BROADCAST_CORNER_INDICATOR)) {
                    int visibility = bundle.getInt(CheckForNewDataService.VISIBILITY);
                    if (visibility == 0) avi.setVisibility(View.INVISIBLE);
                    else if (visibility == 1) avi.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    private boolean isImplicitIntent = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (!getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            registerReceiver(newDataReciver, new IntentFilter(CheckForNewDataService.PACKGE_NAME));
            CheckForNewDataService.startActionCheckNewData(this, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!getIntent().hasExtra(Intent.EXTRA_STREAM)) unregisterReceiver(newDataReciver);
    }

    public void to_reminder() {
        Intent myIntent = new Intent(this, TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, TimerReceiver.NOTIFICATION, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, prefs.getInt("notihour", MySettingsActivity.notihour)/*timepicker.getCurrentHour()*/);
        cal.set(Calendar.MINUTE, prefs.getInt("notiminute", MySettingsActivity.notiminute)/*timepicker.getCurrentMinute()*/);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTime().getTime(), 24 * 60 * 60 * 1000, pendingIntent);
    }


    static AVLoadingIndicatorView addCenterIndicator(Activity activity) {
        AVLoadingIndicatorView statusView = new AVLoadingIndicatorView(activity, Color.parseColor("#03A9F4"));
        FrameLayout.LayoutParams h = new FrameLayout.LayoutParams(130, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        activity.addContentView(statusView, h);
        return statusView;
    }

    static void removeCenterIndicator(AVLoadingIndicatorView statusView) {
        if (statusView != null && statusView.getParent() != null)
            ((ViewGroup) statusView.getParent()).removeView(statusView);
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(appKeyPair, ACCESS_TOKEN);
        // I guess then you just have to instantiate a DropboxAPI object and you're good to go without the startAuthentication()... endAuthentication() etc.

        //DropboxAPI<AndroidAuthSession> mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        //AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        //loadAuth(session);
        return session;
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        error.show();
    }

    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") || APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");

            return;
        }

        // Check if the app has set up its manifest properly.
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        String scheme = "db-" + APP_KEY;
        String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
        testIntent.setData(Uri.parse(uri));
        PackageManager pm = getPackageManager();
        if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
            showToast("URL scheme in your app's " +
                    "manifest is not set up correctly. You should have a " +
                    "com.dropbox.client2.android.AuthActivity with the " +
                    "scheme: " + scheme);

        }
    }

    public static final String ACCESS_TOKEN = "YbyokGh9a44AAAAAAAAGeHQxY3WBt1QvMcXKkFS92RnQRPdHrFRg4DaJGbqut_c5";

    private static final String APP_KEY = "rtn2ydcn5ed3els";
    private static final String APP_SECRET = "0ouukhzlg3e3mcp";

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getApplicationContext().getSharedPreferences(Log_in.ACCOUNT_PREFS_NAME, MODE_PRIVATE);

        if (getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            if (prefs.getBoolean("isAdmin", false)) {
                if (isNetworkAvailable()) {
                    setContentView(R.layout.animatedcircleloadingview);
                    AnimatedCircleLoadingView circleLodaing = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
                    AndroidAuthSession session = buildSession();
                    DropboxAPI<AndroidAuthSession> mApi = new DropboxAPI<AndroidAuthSession>(session);
                    checkAppKeySetup();

                    Uri fileURI = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
                    //       InputStream inputStream = getContentResolver().openInputStream(fileURI);
                    File file = new File(MainActivity.getPath(this, fileURI));
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss", Locale.US);
                    String strDate = sdf.format(c.getTime());
                    String name = strDate + file.getName().substring(file.getName().lastIndexOf("."));
                    Log.e("dfghdfh", name);
                    isImplicitIntent = true;
                    UploadPicture upload = new UploadPicture(this, mApi, Log_in.APP_DIR + "/images/", file, /*file.getName()*//*"pic-"+BROADCAST_NEW_DATA_IMAGES.imagePath.length+1*/
                            name, isImplicitIntent, circleLodaing);

                    upload.execute();

                } else {
                    Toast.makeText(getApplicationContext(), "لايوجد اتصال أنترنت", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "عليك تسجيل الدخول كمدير", Toast.LENGTH_LONG).show();
            }
        } else {
            setContentView(R.layout.activity_main);
            mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
            setSupportActionBar(mToolbar);
            avi = (AVLoadingIndicatorView) findViewById(R.id.av_toolbar);
            //avi.setVisibility(View.VISIBLE);
            //mToolbar.setBackgroundResource(R.drawable.toolbar11);
            //mToolbar.setBackgroundResource(R.drawable.toolbar);
            mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);

            // Set up the drawer.
            mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
            // populate the navigation drawer

            encodedCoverImage = prefs.getString(MySettingsActivity.PREF_KEY_COVER_PIC, null);
            if (encodedCoverImage == null) {
                mNavigationDrawerFragment.setUserData(Log_in.userlogininfo.optString("user_name"), "fahyaynaho@gmail.com", BitmapFactory.decodeResource(getResources(), R.drawable.pic7));
            } else // there's a saved pic
            {
          /*  BitmapFactory.Options options = new BitmapFactory.Options();
            byte[] decodedCoverImage = Base64.decode(encodedCoverImage,Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedCoverImage, 0,decodedCoverImage.length);*/

                mNavigationDrawerFragment.setUserData(Log_in.userlogininfo.optString("user_name"), "fahyaynaho@gmail.com", /*decodedByte*/decodeBase64(encodedCoverImage));
                //mNavigationDrawerFragment.setUserData("Mohammad Djazmati", "mhdjazmati@gmail.com", BitmapFactory.decodeFile(encodedCoverImage));
            }
            avatar = (ImageView) findViewById(R.id.imgAvatar);
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "لتغيير الصورة اذهب للاعدادت", Toast.LENGTH_SHORT).show();
                }
            });
            to_reminder();
            //showcase
/*
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //if (mNavigationDrawerFragment.isDrawerOpen()) presentShowcaseSequence();
                }
            }, 1000);
*/

            //boolean reload = getIntent().getBooleanExtra("reload", false);
            Log.e("reload", RELOAD + "");
            if (RELOAD) {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_event);
                if (fragment == null) {
                    fragment = new ProgramFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }
            if (getIntent().hasExtra("goToMessages")) {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_performance);
                if (fragment == null) {
                    fragment = new MessageFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            } else if (getIntent().hasExtra("goToDrs")) {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_truck);
                if (fragment == null) {
                    fragment = new MediaFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

            }

        }

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission(READ_EXTERNAL_STORAGE) || !checkIfAlreadyHavePermission(WRITE_EXTERNAL_STORAGE) || !checkIfAlreadyHavePermission(INTERNET) || !checkIfAlreadyHavePermission(VIBRATE) || !checkIfAlreadyHavePermission(ACCESS_WIFI_STATE) || !checkIfAlreadyHavePermission(ACCESS_NETWORK_STATE) || !checkIfAlreadyHavePermission(WAKE_LOCK) || !checkIfAlreadyHavePermission(GET_TASKS)) {
                requestForSpecificPermission();
            }
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, 101);
    }

    private boolean checkIfAlreadyHavePermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(1000); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, Log_in.SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();
            }
        });
        sequence.setConfig(config);
        //sequence.addSequenceItem(mButtonOne, "This is button one", "GOT IT");
        View childAt = NavigationDrawerFragment.mDrawerList.getChildAt(2);

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(childAt).setDismissText("GOT IT").setTitleText(R.string.welcomecase).setDismissOnTargetTouch(true).setTargetTouchable(true).setDismissTextColor(Color.GREEN).setContentText(R.string.linimage).build());
/*

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(_nameText)
                        .setTitleText(R.string.signuptitle)
                        .setDismissText(R.string.got_it)
                        .setContentText(R.string._nametext)
                        .withRectangleShape()
                        .setDismissTextColor(Color.GREEN)
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(_passwordText1)
                        .setTitleText(R.string.signuptitle)
                        .setDismissText(R.string.got_it)
                        .setContentText(R.string._passwordText1)
                        .withRectangleShape(true)
                        .setDismissTextColor(Color.GREEN)
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(_passwordText2)
                        .setTitleText(R.string.signuptitle)
                        .setDismissText(R.string.got_it)
                        .setContentText(R.string._passwordText2)
                        .withRectangleShape()
                        .setDismissTextColor(Color.GREEN)
                        .build()

        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(_signupButton)
                        .setTitleText(R.string.signuptitle)
                        .setDismissText(R.string.got_it)
                        .setContentText(R.string._signupButton)
                        .withRectangleShape(true)
                        .setDismissTextColor(Color.GREEN)
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(user_name)
                        .setDismissText(R.string.got_it)
                        .setContentText(R.string.user_name)
                        .withRectangleShape(true)
                        .setDismissTextColor(Color.GREEN)
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(pword)
                        .setDismissText(R.string.got_it)
                        .setContentText(R.string.pword)
                        .withRectangleShape(true)
                        .setDismissTextColor(Color.GREEN)
                        .build()
        );
        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(btnSignIn)
                        .setDismissText(R.string.got_it)
                        .setContentText(R.string.btnSignIn)
                        .withRectangleShape(true)
                        .setDismissTextColor(Color.GREEN)
                        .build()
        );
*/
        sequence.start();

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    int pos = 99;

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;
        pos = position;
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (Log_in.isAdmin) {
            switch (position) {
                case 0: //images//
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_images);
                    if (fragment == null) {
                        fragment = new ImagesFragment();

                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("معرض الصور");
                    break;
                case 1: //drivers
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_driver);
                    if (fragment == null) {
                        fragment = new UserFragment();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("مستخدمون");
                    break;
                case 2: //trucks //
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_truck);
                    if (fragment == null) {
                        fragment = new MediaFragment();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("دروس");
                    break;
                case 3: //performance //
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_performance);
                    if (fragment == null) {
                        fragment = new MessageFragment();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("الرسائل");
                    break;
                case 4: //events//
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_event);
                    if (fragment == null) {
                        fragment = new ProgramFragment();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("البرنامج");
                    break;
                case 5: //events//
                    Intent i = new Intent(this, MySettingsActivity.class);
                    startActivity(i);
                    break;
            }
        } else {
            switch (position) {
                case 0: //images//
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_images);
                    if (fragment == null) {
                        fragment = new ImagesFragment();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("معرض الصور");
                    break;
                case 1: //trucks //
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_truck);
                    if (fragment == null) {
                        fragment = new MediaFragment();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("دروس");
                    break;
                case 2: //performance //
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_performance);
                    if (fragment == null) {
                        fragment = new MessageFragment();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("الرسائل");
                    break;
                case 3: //events//
                    fragment = getFragmentManager().findFragmentById(R.id.fragment_event);
                    if (fragment == null) {
                        fragment = new ProgramFragment();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    if (actionBar != null) actionBar.setTitle("البرنامج");
                    break;
                case 4: //events//
                    Intent i = new Intent(this, MySettingsActivity.class);
                    startActivity(i);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isImplicitIntent) {
            isImplicitIntent = false;
            finish();
            return;
        }
        if (mNavigationDrawerFragment.isDrawerOpen()) mNavigationDrawerFragment.closeDrawer();
        else {
            // mhdjazmati MODS
            // ****************************************************************
            /*AlertDialog.Builder alertbox = new AlertDialog.Builder(this, R.style.Base_Theme_AppCompat_Light_Dialog);
            alertbox.setTitle("Are you sure you want to exit the application?");
            alertbox.setCancelable(false);
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    //       generate_Performance_file();
                    //   android.os.Process.killProcess(android.os.Process.myPid());
                    finish();
                }
            });
            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            alertbox.show();*/
            mNavigationDrawerFragment.openDrawer();
            if (exit) {
                finish();
            } else {
                Toast.makeText(this, "اضغط رجوع مرة ثانية للخروج", Toast.LENGTH_LONG).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 4 * 1000);
            }
        }
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.
                if (Log_in.isAdmin) {
                    if (pos == 0) getMenuInflater().inflate(R.menu.main_image, menu);
                    else if (pos == 1) getMenuInflater().inflate(R.menu.main_user, menu);
                    else if (pos == 2) getMenuInflater().inflate(R.menu.droos, menu);
                    else if (pos == 3) getMenuInflater().inflate(R.menu.messages, menu);
                    else getMenuInflater().inflate(R.menu.main, menu);
                }
                else {
                    getMenuInflater().inflate(R.menu.main, menu);
                }
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

                Intent i = new Intent(this, MySettingsActivity.class);
                startActivity(i);
            }
            else if (id == R.id.add) {
                Intent i = new Intent(this, Add_user.class);
                startActivity(i);
            }
            else if (id == R.id.see_requests) {
                Intent i = new Intent(this, UserRequests.class);
                startActivity(i);
            }
            else if (id == R.id.add_drs) {
                Intent i = new Intent(this, Add_drs.class);
                startActivity(i);
            }
            else if (id == R.id.add_message) {
                Intent i = new Intent(this, Add_message.class);
                startActivity(i);
            }
            else if (id == R.id.add_image) {
                changeCoverPic_Clicked();
            }
            return super.onOptionsItemSelected(item);
        }
    */
    public void changeCoverPic_Clicked() {
        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mediaIntent.setType("image/*"); //set mime type as per requirement
        startActivityForResult(mediaIntent, IMAGE_REQUEST_CODE);
    }
/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri fileURI = data.getData();
            //       InputStream inputStream = getContentResolver().openInputStream(fileURI);
            File file = new File(getPath(MainActivity.this, fileURI));
            String format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(new Date());
            format += System.currentTimeMillis();
            Log.e("CurrentDateTime", format);
            UploadPicture upload = new UploadPicture(MainActivity.this, Log_in.mApi, Log_in.APP_DIR + "/images/", file, */
/*file.getName()*//*
*/
/*"pic-"+BROADCAST_NEW_DATA_IMAGES.imagePath.length+1*//*
format);
            upload.execute();
        }

    }
*/

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
