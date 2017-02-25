package querytest.test.samer.myapplicationssss;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Pattern;

import io.techery.scalablecropp.library.Crop;
import io.techery.scalablecropp.library.Utils;


public class MySettingsActivity extends ActionBarActivity implements Crop.ImageCropListener {

    private static final int IMAGE_REQUEST_CODE = 1;
    public static final String PREF_KEY_COVER_PIC = "cover_pic_key";
    public String mErrorMsg;
    public static int notihour = 0;
    public static int notiminute = 0;
    TimePicker TP;
    public File mFileTemp;
    CheckBox autoSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        TP = (TimePicker) findViewById(R.id.timePicker);
        TP.setCurrentHour(MainActivity.prefs.getInt("notihour", notihour));
        TP.setCurrentMinute(MainActivity.prefs.getInt("notiminute", notiminute));

        autoSignIn = (CheckBox) findViewById(R.id.auto_sigh_in);
        autoSignIn.setChecked(MainActivity.prefs.getBoolean("autoSignIn",false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor edit = MainActivity.prefs.edit();
        edit.putInt("notihour",notihour=TP.getCurrentHour());
        edit.putInt("notiminute",notiminute=TP.getCurrentMinute());

        edit.putBoolean("autoSignIn",autoSignIn.isChecked());
        edit.putString("pwrd",Log_in.m_password);

        edit.apply();
        to_reminder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_settings, menu);
        return true;
    }

    public void to_reminder() {
        Intent myIntent = new Intent(this, TimerReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, TimerReceiver.NOTIFICATION, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, MySettingsActivity.notihour/*timepicker.getCurrentHour()*/);
        cal.set(Calendar.MINUTE, MySettingsActivity.notiminute/*timepicker.getCurrentMinute()*/);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTime().getTime(), 24 * 60 * 60 * 1000, pendingIntent);

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

    public void changeCoverPic_Clicked(View view) {

        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);

        mediaIntent.setType("image/*"); //set mime type as per requirement

        startActivityForResult(mediaIntent, IMAGE_REQUEST_CODE);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    private void createTempFileCopy(File sourceFile) {
        String tempFileName = sourceFile.getName();
            mFileTemp = new File(getCacheDir(), tempFileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri fileURI = data.getData();
//
            File sourceFile = new File(MainActivity.getPath(this, fileURI));
            createTempFileCopy(sourceFile);
            InputStream inputStream = null; // Got the bitmap .. Copy it to the temp file for cropping
            try {
                inputStream = getContentResolver().openInputStream(fileURI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Utils.copy(inputStream, mFileTemp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Crop.prepare(mFileTemp.getPath()).ratio(1,1).startFrom(this);


            //Log.e("SHARED_PREF ADDED", MainActivity.getPath(this, fileURI));
           // int bytes = bitmap.getByteCount();
           // ByteBuffer byteBuffer = ByteBuffer.allocate(bytes);
           // bitmap.copyPixelsFromBuffer(byteBuffer);
           // byte[] array = byteBuffer.array();
           // String photoData = Base64.encodeToString(array, Base64.DEFAULT);


            //editor.putString(PREF_KEY_COVER_PIC,getRealPathFromURI(this,fileURI));
            //MainActivity.mNavigationDrawerFragment.setUserData("Mohammad Djazmati", "mhdjazmati@gmail.com", bitmap);
            //new AsyncLogin().execute(bitmap);
        }
        else if (Crop.onActivityResult(requestCode, resultCode, data, this)) {

        }
    }

    @Override
    public void onImageCropped(String filePath, String error) {
        Log.e("cropped file path",filePath);
//            SharedPreferences pref = getApplicationContext().getSharedPreferences("TEST_NAME", MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//            String tobase64 = encodeTobase64(bitmap);
//            editor.putString(PREF_KEY_COVER_PIC, tobase64);
//            editor.apply();

            //File file = new File(MainActivity.getPath(this, fileURI));
            String pictureName = Log_in.userlogininfo.optString("user_name")+"."+ mFileTemp.getName().split(Pattern.quote("."))[1] ;
            UploadPicture upload = new UploadPicture(this, Log_in.mApi, Log_in.APP_DIR + "/pics/",mFileTemp
                    , pictureName,true);
            upload.execute();


    }

    private class AsyncLogin extends AsyncTask<String, Void, Void> {

        public ProgressDialog statusDialog;

        @Override
        protected Void doInBackground(String... params) {

            try {
                // Get the metadata for a directory
//                DropboxAPI.Entry dirent = Log_in.mApi.metadata(Log_in.APP_DIR+"pics/", 1000, null, true, null);


                String newFile = Log_in.userlogininfo.opt("user_name") + ".txt";

                String outPath = new File(getCacheDir(), newFile).getPath();
                File outFile = new File(outPath);
                FileWriter fileWriter;
                fileWriter = new FileWriter(outFile);
                fileWriter.write(params[0]);// writing JSON list objects on file as string
                fileWriter.flush();
                fileWriter.close();
                FileInputStream fis = new FileInputStream(outFile);
                String path = Log_in.APP_DIR + "pics/" + outFile.getName();
                final DropboxAPI.UploadRequest uploadRequest;

                uploadRequest = Log_in.mApi.putFileOverwriteRequest(path, fis, outFile.length(), null);
                uploadRequest.upload();

                return null;
            } catch (DropboxUnlinkedException e) {
                // The AuthSession wasn't properly authenticated or user unlinked.
                mErrorMsg = "The AuthSession wasn't properly authenticated or user unlinked.";
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
                mErrorMsg = "Network error.  Try again.";
            } catch (DropboxParseException e) {
                // Probably due to Dropbox server restarting, should retry
                mErrorMsg = "Dropbox error.  Try again.";
            } catch (DropboxException e) {
                // Unknown error
                mErrorMsg = "Unknown error.  Try again.";
            } catch (IOException e) {
                e.printStackTrace();
                mErrorMsg = e + "";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusDialog = new ProgressDialog(MySettingsActivity.this);
            statusDialog.setMessage("جار رفع الصورة .....");
            statusDialog.setIndeterminate(false);
            statusDialog.setCancelable(false);
            statusDialog.show();
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         * @param aVoid The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            statusDialog.dismiss();
            if (mErrorMsg != null) {
                Toast.makeText(MySettingsActivity.this, mErrorMsg, Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(MySettingsActivity.this, "تم", Toast.LENGTH_SHORT).show();

        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
