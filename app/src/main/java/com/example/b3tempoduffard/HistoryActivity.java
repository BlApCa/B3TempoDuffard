package com.example.b3tempoduffard;

import static com.example.b3tempoduffard.MainActivity.edfApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.b3tempoduffard.databinding.ActivityHistoryBinding;


import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = HistoryActivity.class.getSimpleName();
    public String beginDate = Tools.getNowDate("yyyy");
    public String endDate = Tools.getNextYear("yyyy");


    // init views
    ActivityHistoryBinding binding;

    // data model
    List<TempoDate> tempoDates = new ArrayList<>() ;

    // RecyclerView adapter
    TempoDateAdapter tempoDateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init recycler view
        binding.tempoHistoryRv.setHasFixedSize(true);
        binding.tempoHistoryRv.setLayoutManager(new LinearLayoutManager(this));
        tempoDateAdapter = new TempoDateAdapter(tempoDates,this);
        binding.tempoHistoryRv.setAdapter(tempoDateAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tempoHistoryPb.setVisibility(View.VISIBLE);
        if(edfApi != null) {
            getTempoHistory();
        }
    }

    public void getTempoHistory() {
        Call<TempoHistory> call = edfApi.getTempoHistory(beginDate,endDate);
        call.enqueue(new Callback<TempoHistory>() {
            @Override
            public void onResponse(Call<TempoHistory> call, Response<TempoHistory> response) {
                tempoDates.clear();
                if(response.code() == HttpURLConnection.HTTP_OK && response.body() != null ) {
                    tempoDates.addAll(response.body().getTempoDates());
                    Log.d(LOG_TAG,"nb elements = " + tempoDates.size());
                    binding.tempoHistoryPb.setVisibility(View.GONE);
                }
                tempoDateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TempoHistory> call, Throwable t) {

            }
        });

    }
}