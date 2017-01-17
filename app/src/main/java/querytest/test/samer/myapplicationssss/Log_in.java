package querytest.test.samer.myapplicationssss;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxFileSizeException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import com.nineoldandroids.animation.ObjectAnimator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.cketti.library.changelog.ChangeLog;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class Log_in extends ActionBarActivity {
    public static final String ACCESS_TOKEN = "YbyokGh9a44AAAAAAAAGeHQxY3WBt1QvMcXKkFS92RnQRPdHrFRg4DaJGbqut_c5";
    public static final String SHOWCASE_ID = "show_case";
    private String mErrorMsg;
    private MenuItem link;
    public static JSONObject userlogininfo;
    public static JSONObject userprogram;
    public static JSONArray dates;
    public Dialog dialog;
    public static boolean isAdmin;

    private static final String TAG = "FAhyaynaho";

    ///////////////////////////////////////////////////////////////////////////
    //                      Your app-specific settings.                      //
    ///////////////////////////////////////////////////////////////////////////

    // Replace this with your app key and secret assigned by Dropbox.
    // Note that this is a really insecure way to do this, and you shouldn't
    // ship code which contains your key & secret in such an obvious way.
    // Obfuscation is good.
    private static final String APP_KEY = "rtn2ydcn5ed3els";
    private static final String APP_SECRET = "0ouukhzlg3e3mcp";

    ///////////////////////////////////////////////////////////////////////////
    //                      End app-specific settings.                       //
    ///////////////////////////////////////////////////////////////////////////

    // You don't need to change these, leave them alone.
    static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private static final boolean USE_OAUTH1 = false;

    static DropboxAPI<AndroidAuthSession> mApi;


    private boolean mLoggedIn;
    ImageView j;
    // Android widgets
    private Button mSubmit;
    private LinearLayout mDisplay;
    private Button mPhoto;
    private Button mRoulette;

    private ImageView mImage;

    static final String APP_DIR = "/FAhyaynaho/";

    private static final int NEW_PICTURE = 1;
    private String mCameraFileName;


    /**
     * ********************
     * Downloading file fields
     * *********************
     */

    String mPath;
    FileOutputStream mFos;
    Long mFileLen;


    // this activity for logging in process
    ActionProcessButton btnSignIn;
    ActionProcessButton _signupButton;
    LinearLayout linimage;
    int mProgress = 50;
    private TextView message;
    private EditText user_name, pword;
    static SharedPreferences prefs;
    String cachePath;

    static String m_username;
    static String m_password;
    ImageView sw;
    public static JSONObject jsonResponse;
    public MaterialShowcaseSequence sequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        // setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));
        //WebAuthSession sourceSession = new WebAuthSession(state.appKey, Session.AccessType.DROPBOX, sourceAccess);
        //DropboxAPI<?> sourceClient = new DropboxAPI<WebAuthSession>(sourceSession);

/*

        Spanned notiContent = Html.fromHtml("<b>" + "محممممد" + "</b><br>" + "qqqqqqqqqqqqqqqqqqqqqwwwwwww<br>wwwwweeeeeeeeeeeeeeeeeerrrrrrrr<br>rrrrrrrrrrrrrrrrdfgdfgdfgdfgdfg");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 13631, new Intent(getApplicationContext(), Log_in.class), PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(pIntent);
        builder.setContentTitle("رسائل جديدة !");
        builder.setTicker("رسائل جديدة !");
        builder.setContentText(notiContent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notiContent)); // Makes it expandable to show the message
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{200, 150, 50, 33, 0});
        builder.setOngoing(false);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE); //what does this do!?
        if (Build.VERSION.SDK_INT < 16) {
            notificationManager.notify(13631, builder.getNotification());
        }
        else {
            notificationManager.notify(13631, builder.build());
        }
*/

        AndroidAuthSession session = buildSession();
        mApi = new DropboxAPI<AndroidAuthSession>(session);
        _nameText = (EditText) findViewById(R.id.input_name);
        j = (ImageView) findViewById(R.id.logo);
        j.setVisibility(View.INVISIBLE);
        _passwordText1 = (EditText) findViewById(R.id.input_password1);
        _passwordText2 = (EditText) findViewById(R.id.input_password2);
        linimage = (LinearLayout) findViewById(R.id.linimage);
        sw = (ImageView) findViewById(R.id.swipeupimage);
        checkAppKeySetup();
        supl = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        final TextView viewById = (TextView) findViewById(R.id.link_signup);
        supl.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.e(TAG, "onPanelSlide, offset " + slideOffset);
                sw.setAlpha(1 - slideOffset);
                viewById.setAlpha(1 - slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.e(TAG, "onPanelStateChanged " + newState);
                if (newState.toString().equals("COLLAPSE")) {

                }
            }
        });
        message = (TextView) findViewById(R.id.loginErrorMsg);
        user_name = (EditText) findViewById(R.id.user_name);
        pword = (EditText) findViewById(R.id.pword);
        pword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    btnSignIn.performClick();
                    return true;
                }
                return false;
            }
        });
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        _signupButton = (ActionProcessButton) findViewById(R.id.btn_Signup);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        //btnSignIn.getBackground().setAlpha(150);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_username = user_name.getText().toString().trim();
                m_password = pword.getText().toString().trim();
                InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.hideSoftInputFromWindow(btnSignIn.getWindowToken(), 0);
                on_click_login();

            }
        });
        prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, MODE_PRIVATE);
        // Set<String> set = prefs.getStringSet("imagePaths", null);

        setupFilesForOfflineMod();
        // TODO
        user_name.setText(prefs.getString("name", ""));


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMgr.hideSoftInputFromWindow(_signupButton.getWindowToken(), 0);
                signup();
            }
        });
        // MaterialShowcaseView.resetSingleUse(this, SHOWCASE_ID);


        final Handler myhandler = new Handler();
        myhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animation = new TranslateAnimation(0, 0, 5, -11);
                animation.setDuration(500);
                animation.setFillAfter(true);
                TranslateAnimation animation1 = new TranslateAnimation(0, 0, 11, -11);
                animation1.setDuration(500);
                animation1.setStartOffset(1000);
                animation1.setFillAfter(true);
                AnimationSet s = new AnimationSet(false);
                s.addAnimation(animation);
                //s.addAnimation(animation1);
                sw.startAnimation(s);

                myhandler.postDelayed(this, 3000);
            }
        }, 0);

        int curVersion = 0;
        try {
            curVersion = getPackageManager().getPackageInfo("querytest.test.samer.myapplicationssss", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("curVersion", curVersion);
        edit.apply();
    }

    private void setupFilesForOfflineMod() {
        cachePath = prefs.getString("userFilePath", null);
        File fff = null;
        if (cachePath != null) {
            fff = new File(cachePath);
            if (!fff.exists()) {
                cachePath = null;
            }
        }
        isAdmin = prefs.getBoolean("isAdmin", false);
    }

    private boolean isPresentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(1000); // half second between each showcase view

        sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {
                //Toast.makeText(itemView.getContext(), "Item #" + position, Toast.LENGTH_SHORT).show();

                if (position == 5) {
                    supl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }

            }
        });

        sequence.setConfig(config);

        //sequence.addSequenceItem(mButtonOne, "This is button one", "GOT IT");

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(sw)
                //.setDismissText("GOT IT")
                .setTitleText(R.string.welcomecase).setDismissOnTargetTouch(true).setTargetTouchable(true).setContentText(R.string.linimage)

                .build());

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(_nameText).setTitleText(R.string.signuptitle).setDismissText(R.string.got_it).setContentText(R.string._nametext).withRectangleShape().setDismissTextColor(Color.GREEN).build());
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(_passwordText1).setTitleText(R.string.signuptitle).setDismissText(R.string.got_it).setContentText(R.string._passwordText1).withRectangleShape(true).setDismissTextColor(Color.GREEN).build());
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(_passwordText2).setTitleText(R.string.signuptitle).setDismissText(R.string.got_it).setContentText(R.string._passwordText2).withRectangleShape().setDismissTextColor(Color.GREEN).build()

        );
/*        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(_signupButton)
                        .setTitleText(R.string.signuptitle)
                        .setDismissText(R.string.got_it)
                        .setContentText(R.string._signupButton)
                        .withRectangleShape(true)
                        .build()
        );*/
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(_signupButton).setTitleText(R.string.signuptitle).setDismissText(R.string.got_it).setContentText(R.string._signupButton).withRectangleShape(true).setDismissTextColor(Color.GREEN).build());
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(user_name).setDismissText(R.string.got_it).setContentText(R.string.user_name).withRectangleShape(true).setDismissTextColor(Color.GREEN).build());
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(pword).setDismissText(R.string.got_it).setContentText(R.string.pword).withRectangleShape(true).setDismissTextColor(Color.GREEN).build());
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(btnSignIn).setDismissText(R.string.got_it).setContentText(R.string.btnSignIn).withRectangleShape(true).setDismissTextColor(Color.GREEN).build());
        return sequence.hasFired();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mApi.getSession();

        // The next part must be inserted in the onResume() method of the
        // activity from which session.startAuthentication() was called, so
        // that Dropbox authentication completes properly.
        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                //        session.finishAuthentication();

                // Store it locally in our app for later use
                //        storeAuth(session);
                setLoggedIn(true);
            } catch (IllegalStateException e) {
                showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
                Log.i(TAG, "Error authenticating", e);
            }
        }

    }


    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        ObjectAnimator moveAnim = ObjectAnimator.ofFloat(supl, "Y", -1300, 0);

        moveAnim.setDuration(1700);
        moveAnim.setStartDelay(10);
        moveAnim.setInterpolator(new BounceInterpolator());
        moveAnim.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ChangeLog cl = new ChangeLog(Log_in.this);
                //if (!isPresentShowcaseSequence() || cl.isFirstRun()) {
                    ////////////////sequence.start();
                    if(cl.isFirstRun())cl.getLogDialog().show();
                //}
                else {
                    if (prefs.getBoolean("autoSignIn", false)) {
                        pword.setText(prefs.getString("pwrd", ""));
                        m_username = user_name.getText().toString().trim();
                        m_password = pword.getText().toString().trim();
                        on_click_login();

                    }
                }
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animFadeIn = AnimationUtils.loadAnimation(Log_in.this, R.anim.abc_fade_in);
                animFadeIn.setDuration(1500);
                j.startAnimation(animFadeIn);
                j.setVisibility(View.VISIBLE);
/*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        *//*isPresentShowcaseSequence();

                        ChangeLog cl = new ChangeLog(Log_in.this);
                        if (cl.isFirstRun()) {
                            cl.getLogDialog().show();
                        }*//*
                    }
                }, 700);*/
            }
        }, 1900);

    }

    public void on_click_login() {

        if (!m_username.trim().isEmpty() && !m_password.trim().isEmpty()) {
//TODO fix network prossesing after finish with the offline mod
            if (cachePath != null || isNetworkAvailable())// checking Internet access
            {

                btnSignIn.setProgress(mProgress);
                // start logging in task
                message.setText("يتم الولوج إلى المخدم");
                AsyncLogin logintask = new AsyncLogin();
                logintask.execute(m_username, m_password);
            }
            else {
                btnSignIn.setProgress(-1);
                btnSignIn.setText("خطأ انترنت");
                message.setText("لايوجد اتصال بيانات , الرجاء تأكد من الاتصال");
                Toast.makeText(Log_in.this, "لايوجد اتصال بيانات , الرجاء تأكد من الاتصال", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            if (m_username.trim().isEmpty()) {
                user_name.setError("الرجاء ادخال اسم مستخدم");

            }
            else {
                user_name.setError(null);
            }
            if (m_password.trim().isEmpty() || m_password.length() < 3) {
                pword.setError("يجب ان تكون كلمه المرور اكثر من 3 محارف");

            }
            else {
                pword.setError(null);
            }
        }

        startService(new Intent(this, CheckForUpdateService.class));
    }


    public void signUp(View view) {
        dialog = new Dialog(this);
        dialog.setTitle("إنشاء حساب جديد");
        dialog.setContentView(R.layout.layout_alert_dialog_signup);
        dialog.setCancelable(false);
        dialog.show();
        final EditText user = (EditText) dialog.findViewById(R.id.alertusername);
        final EditText pswrd = (EditText) dialog.findViewById(R.id.alertpswrd);
        Button ok = (Button) dialog.findViewById(R.id.button3);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getText().toString().trim().equals("")) {
                    user.setHint("الرجاء ادخل الاسم");
                    return;
                }
                else if (pswrd.getText().toString().trim().equals("")) {
                    pswrd.setHint("الرجاء ادخل كلمه المرور");
                    return;
                }
                String prefix = pswrd.getText().toString().trim() + "--" + user.getText().toString().trim();
                File file = new File(getCacheDir(), prefix);
                dialog.dismiss();
                new AsyncSignUp(file).execute();

            }
        });
        Button cansel = (Button) dialog.findViewById(R.id.button4);
        cansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    boolean flip;

    public void changelogo(View view) {
        if (flip) {
            j.setImageResource(R.drawable.w);
            flip = !flip;

        }
        else {
            j.setImageResource(R.drawable.ww);
            flip = !flip;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private class AsyncLogin extends AsyncTask<String, Void, Void> {
        Boolean loginVerifed = false;
        public AVLoadingIndicatorDialog progressDialog;

        @Override
        protected Void doInBackground(String... params) {
            try {
                boolean isDeifferentName = false;
                boolean isCashFileExists = false;
                if (cachePath != null) {
                    int length = cachePath.split("/").length - 1;
                    String s = cachePath.split("/")[length];
                    String s1 = s.split("\\.")[0];
                    if (!s1.equals(params[0])) {
                        isDeifferentName = true;
                    }
                    File cashFile = new File(cachePath);
                    isCashFileExists = cashFile.exists();
                }

                if (/*isNetworkAvailable()*/cachePath == null || isDeifferentName || !isCashFileExists) {
                    // Get the metadata for a directory
                    DropboxAPI.Entry dirent = mApi.metadata(APP_DIR, 1000, null, true, null);
                    if (!dirent.isDir || dirent.contents == null) {
                        // It's not a directory, or there's nothing in it
                        mErrorMsg = "File or empty directory";
                        //return false;
                    }
                    // Make a list of everything in it that we can get a thumbnail for
                    ArrayList<DropboxAPI.Entry> thumbs = new ArrayList<>();
                    for (DropboxAPI.Entry ent : dirent.contents) {
                        //  if (ent.thumbExists) {
                        //      // Add it to the list of thumbs we can choose from
                        //      thumbs.add(ent);
                        //   }
                        final String replace = ent.fileName().replace(".json", "");
                        if (replace.equals(params[0])) {
                            thumbs.add(ent);
                            String path = ent.path;
                            mFileLen = ent.bytes;
                            break;
                        }

                    }

                    DropboxAPI.Entry dirent1 = mApi.metadata(APP_DIR + "/config/", 1000, null, true, null);
                    if (!dirent1.isDir || dirent1.contents == null) {
                        // It's not a directory, or there's nothing in it
                        isAdmin = false;
                        //return false;
                    }

                    btnSignIn.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setMessage("جار مزامنه البيانات...");
                            btnSignIn.setText("جار مزامنه البيانات...");
                        }
                    });
                    isAdmin = false;
                    for (DropboxAPI.Entry ent : dirent1.contents) {
                        final String replace = ent.fileName().replace(".txt", "");
                        if (replace.equals(params[0])) {
                            isAdmin = true;
                            break;
                        }
                    }


                    if (thumbs.size() == 0) {
                        // No thumbs in that directory
                        mErrorMsg = "No pictures in that directory";
                        loginVerifed = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                btnSignIn.setProgress(-1);
                                btnSignIn.setText("خطأ في الولوج");
                                message.setText("تأكد من اسم المستخم و كلمة المرور");
                                Toast.makeText(Log_in.this, "تأكد من اسم المستخم و كلمة المرور", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return null;

                    }

                    cachePath = Log_in.this.getCacheDir().getAbsolutePath() + "/" + thumbs.get(0).fileName();
                    //String cachePath = Environment.getExternalStorageDirectory().getAbsolutePath()+APP_DIR+ thumbs.get(0).fileName();
                    try {
                        mFos = new FileOutputStream(cachePath);
                    } catch (FileNotFoundException e) {
                        mErrorMsg = "Couldn't create a local file to store the image";
                    }

                    mApi.getFile(thumbs.get(0).path, null, mFos, null);
                    // This downloads a smaller, thumbnail version of the file.  The
                    // API to download the actual file is roughly the same.
                    //    mApi.getThumbnail(path, mFos, DropboxAPI.ThumbSize.BESTFIT_960x640,
                    //            DropboxAPI.ThumbFormat.JPEG, null);

                    //     mDrawable = Drawable.createFromPath(cachePath);
                    // We must have a legitimate picture
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Log_in.this, "تسجيل الدخول في وضع عدم الاتصال", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                File fff = new File(cachePath);

                FileInputStream fIn = new FileInputStream(fff);
                InputStreamReader isr = new InputStreamReader(fIn);
                /* Prepare a char-Array that will
                * hold the chars we read back in. */
                char[] inputBuffer = new char[(int) fff.length()];

                // Fill the Buffer with data from the file
                isr.read(inputBuffer);

                // Transform the chars to a String
                String readString = new String(inputBuffer);
                List<String> allNames = new ArrayList<>();
                jsonResponse = new JSONObject(readString);
                JSONArray user = jsonResponse.getJSONArray("user");
                userlogininfo = user.getJSONObject(0);
                userprogram = user.getJSONObject(1);
                dates = jsonResponse.getJSONArray("dates");

                //    JSONObject jsonfile = new JSONObject(readString);
                if (userlogininfo.optString("user_name").equals(params[0]) && userlogininfo.optString("password").equals(params[1]))
                    loginVerifed = true;
                else {
                    loginVerifed = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            btnSignIn.setProgress(-1);
                            btnSignIn.setText("خطأ في الولوج");
                            message.setText("تأكد من اسم المستخم و كلمة المرور");
                            Toast.makeText(Log_in.this, "تأكد من اسم المستخم و كلمة المرور", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return null;
                }

                isr.close();
                inputBuffer = null;
                if (mFos != null) {
                    mFos.close();
                }
                fIn.close();

                return null;
            } catch (DropboxUnlinkedException e) {
                // The AuthSession wasn't properly authenticated or user unlinked.
                mErrorMsg = "انت لم تتصل بحساب الدروب بوكس بعد , من فضلك اضغط على خيار اتصال الدروب بوكس!";
            } catch (DropboxPartialFileException e) {
                // We canceled the operation
                mErrorMsg = "Download canceled";
            } catch (DropboxServerException e) {
                // Server-side exception.  These are examples of what could happen,
                // but we don't do anything special with them here.
                if (e.error == DropboxServerException._304_NOT_MODIFIED) {
                    // won't happen since we don't pass in revision with metadata
                }
                else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
                    // Unauthorized, so we should unlink them.  You may want to
                    // automatically log the user out in this case.
                }
                else if (e.error == DropboxServerException._403_FORBIDDEN) {
                    // Not allowed to access this
                }
                else if (e.error == DropboxServerException._404_NOT_FOUND) {
                    // path not found (or if it was the thumbnail, can't be
                    // thumbnailed)
                }
                else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
                    // too many entries to return
                }
                else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
                    // can't be thumbnailed
                }
                else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
                    // user is over quota
                }
                else {
                    // Something else
                }
                // This gets the Dropbox error, translated into the user's language
                mErrorMsg = e.body.userError;
                if (mErrorMsg == null) {
                    mErrorMsg = e.body.error;
                }
            } catch (DropboxIOException e) {
                // Happens all the time, probably want to retry automatically.
                mErrorMsg = "خطأ في شبكة الأنترنت , حاول مرة اخرى.";
            } catch (DropboxParseException e) {
                // Probably due to Dropbox server restarting, should retry
                mErrorMsg = "Dropbox error.  Try again.";
            } catch (DropboxException e) {
                // Unknown error
                mErrorMsg = "Unknown error.  Try again.";
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                mErrorMsg = e + "";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (loginVerifed) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("name", m_username);
                edit.putString("userFilePath", cachePath);
                edit.putBoolean("isAdmin", isAdmin);
                edit.apply();
                btnSignIn.setProgress(100);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                if(getIntent().hasExtra("goToMessages")){
                    i.putExtra("goToMessages",true);
                }
                else if(getIntent().hasExtra("goToDrs")){
                    i.putExtra("goToDrs",true);
                }
                // Starting Main-Activity
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();

            }
            else {
                btnSignIn.setProgress(-1);
                btnSignIn.setText("خطأ في الولوج");
                message.setText(mErrorMsg);
                Toast.makeText(Log_in.this, mErrorMsg, Toast.LENGTH_SHORT).show();


            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new AVLoadingIndicatorDialog(Log_in.this);
            progressDialog.setMessage("جار تسجيل الدخول...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            btnSignIn.setProgress(mProgress);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public void onBackPressed() {

        // mhdjazmati MODS
        // ****************************************************************
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog);
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
        alertbox.show();
    }


    private class AsyncSignUp extends AsyncTask<Void, Long, Boolean> {
        public final AVLoadingIndicatorDialog mDialog;
        public final File mFile;
        public DropboxAPI.UploadRequest mRequest;

// We set the context this way so we don't accidentally leak activities

        private AsyncSignUp(File file) {
            mFile = file;

            mDialog = new AVLoadingIndicatorDialog(Log_in.this);
            mDialog.setMessage("جار ارسال الطلب");
            mDialog.setCancelable(false);

            mDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // This will cancel the putFile operation
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //do network action in this function
                            mRequest.abort();
                        }
                    }).start();
                }
            });
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // By creating a request, we get a handle to the putFile operation,
                // so we can cancel it later if we want to
                FileWriter fileWriter;
                fileWriter = new FileWriter(mFile);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();

                FileInputStream fis = new FileInputStream(mFile);
                String path = APP_DIR + "/requests/" + mFile.getName();
                mRequest = mApi.putFileOverwriteRequest(path, fis, mFile.length(), new ProgressListener() {
                    @Override
                    public long progressInterval() {
                        // Update the progress bar every half-second or so
                        return 500;
                    }

                    @Override
                    public void onProgress(long bytes, long total) {
                        publishProgress(bytes);
                    }
                });

                if (mRequest != null) {
                    mRequest.upload();
                    return true;
                }

            } catch (DropboxUnlinkedException e) {
                // This session wasn't authenticated properly or user unlinked
                mErrorMsg = "This app wasn't authenticated properly.";
            } catch (DropboxFileSizeException e) {
                // File size too big to upload via the API
                mErrorMsg = "This file is too big to upload";
            } catch (DropboxPartialFileException e) {
                // We canceled the operation
                mErrorMsg = "Upload canceled";
            } catch (DropboxServerException e) {
                // Server-side exception.  These are examples of what could happen,
                // but we don't do anything special with them here.
                if (e.error == DropboxServerException._401_UNAUTHORIZED) {
                    // Unauthorized, so we should unlink them.  You may want to
                    // automatically log the user out in this case.
                }
                else if (e.error == DropboxServerException._403_FORBIDDEN) {
                    // Not allowed to access this
                }
                else if (e.error == DropboxServerException._404_NOT_FOUND) {
                    // path not found (or if it was the thumbnail, can't be
                    // thumbnailed)
                }
                else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
                    // user is over quota
                }
                else {
                    // Something else
                }
                // This gets the Dropbox error, translated into the user's language
                mErrorMsg = e.body.userError;
                if (mErrorMsg == null) {
                    mErrorMsg = e.body.error;
                }
            } catch (DropboxIOException e) {
                // Happens all the time, probably want to retry automatically.
                mErrorMsg = "Network error.  Try again.";
            } catch (DropboxParseException e) {
                // Probably due to Dropbox server restarting, should retry
                mErrorMsg = "Dropbox error.  Try again.";
            } catch (DropboxException e) {
                // Unknown error
                mErrorMsg = "Unknown error.  Try again.";
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            mDialog.dismiss();
            _signupButton.setEnabled(true);
            if (result) {
                showToast("تم ارسال الطلب , الرجاء الانتظار حتى يتم القبول");
                supl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            else {
                // Log.e("DropBox",mErrorMsg);
                _signupButton.setEnabled(true);
                showToast(mErrorMsg);
            }

        }


/*
        @Override
        protected void onProgressUpdate(Long... progress) {
            int percent = (int) (100.0 * (double) progress[0] / mFile.length() + 0.5);
            mDialog.setProgress(percent);
        }
*/

    }

    SlidingUpPanelLayout supl;


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        String prefix = _passwordText1.getText().toString().trim() + "--" + _nameText.getText().toString().trim();
        File file = new File(getCacheDir(), prefix);
        new AsyncSignUp(file).execute();
    }


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "فشل التسجيل", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    EditText _nameText;
    EditText _passwordText1;
    EditText _passwordText2;

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString().trim();
        String password1 = _passwordText1.getText().toString().trim();
        String password2 = _passwordText2.getText().toString().trim();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("ادخل على الأقل 3 محارف");
            valid = false;
        }
        else {
            _nameText.setError(null);
        }

        if (password1.isEmpty() || password1.length() < 3) {
            _passwordText1.setError("ادخل على الاقل 3 محارف");
            valid = false;
        }
        else {
            _passwordText1.setError(null);
        }

        if (password2.isEmpty() || !password1.equals(password2)) {
            _passwordText2.setError("تأكيد كلمه المرور غير متطابقة مع كلمه المرور");
            valid = false;
        }
        else {
            _passwordText2.setError(null);
        }

        return valid;
    }

    /**
     * Convenience function to change UI state based on being logged in
     */
    private void setLoggedIn(boolean loggedIn) {
        mLoggedIn = loggedIn;
        if (loggedIn) {
            link.setTitle("اقطع الربط");
            //mDisplay.setVisibility(View.VISIBLE);
        }
        else {
            link.setTitle("أربط البرنامج");
            //mDisplay.setVisibility(View.GONE);
            //mImage.setImageDrawable(null);
        }
    }

    private void checkAppKeySetup() {
        // Check to make sure that we have a valid app key
        if (APP_KEY.startsWith("CHANGE") || APP_SECRET.startsWith("CHANGE")) {
            showToast("You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.");
            finish();
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
            finish();
        }
    }

    private void showToast(String msg) {

        Toast error = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        error.show();
    }


    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.apply();
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

}

