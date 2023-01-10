package com.example.b3tempoduffard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.b3tempoduffard.databinding.ActivityMainBinding;

import java.net.HttpURLConnection;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static final String CHANNEL_ID = "tempo_alert_channel_id";
    private static final int ALARM_MANAGER_REQUEST_CODE = 2023;
    public String currentDate = Tools.getNowDate("yyyy-MM-dd");

    public static IEdfApi edfApi;
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.historyBt.setText(getString(R.string.histo_btn_v1));
        binding.historyBtV2.setText(R.string.histo_btn_v2);
        binding.historyBt.setOnClickListener(this);
        binding.historyBtV2.setOnClickListener(this);


        // Create notification channel
        createNotificationChannel();

        // init tempo alarm
        initAlarmManager();

        // Init Retrofit client
        Retrofit retrofitClient = ApiClient.get();
        if (retrofitClient != null) {
            // Create EDF API Call interface
            edfApi = retrofitClient.create(IEdfApi.class);
        } else {
            Log.e(LOG_TAG, "unable to initialize Retrofit client");
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Create call to getTempoDaysLeft
        getTempoDaysLeft();

        // Call to getTempoDaysColor
        getTempoDaysColor();
    }

    //    public void showHistory(View view) {
//        Intent intent = new Intent();
//        intent.setClass(this, HistoryActivity.class);
//        startActivity(intent);
//    }
//
    @Override
    public void onClick(View v) {

        if (v.getId() == binding.historyBt.getId()) {
            Intent intent = new Intent();
            intent.setClass(this, HistoryActivity.class);
            startActivity(intent);
        }
        if (v.getId() == binding.historyBtV2.getId()) {
            Intent intent = new Intent();
            intent.setClass(this, HistoryActivityV2.class);
            startActivity(intent);
        }

    }
    //
    public void getTempoDaysLeft() {

        Call<TempoDaysLeft> call = edfApi.getTempoDaysLeft(IEdfApi.EDF_TEMPO_API_ALERT_TYPE);

        call.enqueue(new Callback<TempoDaysLeft>() {
            @Override
            public void onResponse(@NonNull Call<TempoDaysLeft> call, @NonNull Response<TempoDaysLeft> response) {
                TempoDaysLeft tempoDaysLeft = response.body();
                if (response.code() == HttpURLConnection.HTTP_OK && tempoDaysLeft != null) {
                    Log.d(LOG_TAG, "nb red days = " + tempoDaysLeft.getParamNbJRouge());
                    Log.d(LOG_TAG, "nb white days = " + tempoDaysLeft.getParamNbJBlanc());
                    Log.d(LOG_TAG, "nb blue days = " + tempoDaysLeft.getParamNbJBleu());
                    binding.blueDaysTv.setText(String.valueOf(tempoDaysLeft.getParamNbJBleu()));
                    binding.whiteDaysTv.setText(String.valueOf(tempoDaysLeft.getParamNbJBlanc()));
                    binding.redDaysTv.setText(String.valueOf(tempoDaysLeft.getParamNbJRouge()));
                } else {
                    Log.w(LOG_TAG, "call to getTempoDaysLeft () failed with error code " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TempoDaysLeft> call, @NonNull Throwable t) {
                Log.e(LOG_TAG, "call to getTempoDaysLeft () failed ");
            }
        });

    }
    public void getTempoDaysColor() {

        Call<TempoDaysColor> call2;
        call2 = edfApi.getTempoDaysColor(currentDate,IEdfApi.EDF_TEMPO_API_ALERT_TYPE);

        call2.enqueue(new Callback<TempoDaysColor>() {
            @Override
            public void onResponse(@NonNull Call<TempoDaysColor> call, @NonNull Response<TempoDaysColor> response) {
                TempoDaysColor tempoDaysColor = response.body();
                if (response.code() == HttpURLConnection.HTTP_OK && tempoDaysColor != null) {
                    Log.d(LOG_TAG,"Today color = "+tempoDaysColor.getCouleurJourJ());
                    Log.d(LOG_TAG,"Tomorrow color = "+tempoDaysColor.getCouleurJourJ1());
                    binding.todayDcv.setDayCircleColor(tempoDaysColor.getCouleurJourJ1());
                    binding.todayDcv.setCaptionTextColorDay(getString(tempoDaysColor.getCouleurJourJ().getStringResId()));
                    binding.tomorrowDcv.setCaptionTextColorDay(getString(tempoDaysColor.getCouleurJourJ1().getStringResId()));
                    binding.tomorrowDcv.setDayCircleColor(tempoDaysColor.getCouleurJourJ());
                } else {
                    Log.w(LOG_TAG, "call to getTempoDaysColor() failed with error code " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TempoDaysColor> call, @NonNull Throwable t) {
                Log.e(LOG_TAG, "call to getTempoDaysColor() failed ");
            }
        });
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkColor4Notif(TempoColor color) {
        if (color == TempoColor.RED || color == TempoColor.WHITE) {
            // create notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher) // mandatory setting !
                    .setContentTitle(getString(R.string.tempo_notif_title))
                    .setContentText(getString(R.string.tempo_notif_text, getString(color.getStringResId())))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            // show notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(Tools.getNextNotifId(), builder.build());
        }

    }

    private void initAlarmManager() {

        // create a pending intent
        Intent intent = new Intent(this, TempoAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                this,
                ALARM_MANAGER_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE,49);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}