package querytest.test.samer.myapplicationssss;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.List;

public class SalahTimerReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION = 3456;

    /*since you're always doing a 1-time notification, we can make this final and static, the number
     won't change. If you want it to change, consider using SharedPreferences or similar to keep track
     of the number. You would have the same issue with a Service since you call stopself() and so,
     you would delete the object every time.*/
    public boolean isMainActivityRunning(String packageName, Context c) {
        ActivityManager activityManager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (int i = 0; i < tasksInfo.size(); i++) {
            if (tasksInfo.get(i).baseActivity.getPackageName().equals(packageName)) return true;
        }

        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isMainActivityRunning("querytest.test.samer.myapplicationssss", context)) {
            Intent resultIntent = new Intent(context, Log_in.class);

            PendingIntent pIntent = PendingIntent.getActivity(context, NOTIFICATION, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pIntent);
            builder.setContentTitle("فأحييناه");
            builder.setTicker("تذكرة لتسجيل البرنامج");
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            builder.setContentText("تذكرة لتسجيل البرنامج");
            builder.setVibrate(new long[]{0, 300, 50, 200, 0});
            builder.setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); //what does this do!?
            if (Build.VERSION.SDK_INT < 16) {
                notificationManager.notify(NOTIFICATION, builder.getNotification());
            }
            else {
                notificationManager.notify(NOTIFICATION, builder.build());
            }
        }

    }
}