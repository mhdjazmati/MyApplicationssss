package querytest.test.samer.myapplicationssss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MySettingsActivity extends ActionBarActivity {

    private static final int IMAGE_REQUEST_CODE = 1;
    public static final String PREF_KEY_COVER_PIC = "cover_pic_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_settings, menu);
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

    public void changeCoverPic_Clicked(View view) {

        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);

        mediaIntent.setType("image/*"); //set mime type as per requirement

        startActivityForResult(mediaIntent, IMAGE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri fileURI = data.getData();
            SharedPreferences pref = getApplicationContext().getSharedPreferences("TEST_NAME", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            Log.d("SHARED_PREF ADDED",getRealPathFromURI(this,fileURI));
            editor.putString(PREF_KEY_COVER_PIC,getRealPathFromURI(this,fileURI));
            editor.apply();
           MainActivity.mNavigationDrawerFragment.setUserData("Mohammad Djazmati", "mhdjazmati@gmail.com", BitmapFactory.decodeFile(getRealPathFromURI(this,fileURI)));


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
