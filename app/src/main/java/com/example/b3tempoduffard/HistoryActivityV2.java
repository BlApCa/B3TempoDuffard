package com.example.b3tempoduffard;

import static com.example.b3tempoduffard.MainActivity.edfApi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.b3tempoduffard.databinding.ActivityHistoryV2Binding;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivityV2 extends AppCompatActivity {

    private static final String LOG_TAG = HistoryActivity.class.getSimpleName();
    public String beginDate = Tools.getPastYear();
    public String endDate = Tools.getNowDate("yyyy");


    // init views
    ActivityHistoryV2Binding binding;

    // data model
    List<TempoDate> tempoDates = new ArrayList<>() ;

    // RecyclerView adapter
    TempoDateAdapter tempoDateAdapterV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
        binding = ActivityHistoryV2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init recycler view
        binding.tempoHistoryV2Rv.setHasFixedSize(true);
        binding.tempoHistoryV2Rv.setLayoutManager(new LinearLayoutManager(this));
        tempoDateAdapterV2 = new TempoDateAdapter(tempoDates,this);
        binding.tempoHistoryV2Rv.setAdapter(tempoDateAdapterV2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tempoHistoryV2Pb.setVisibility(View.VISIBLE);
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
                    binding.tempoHistoryV2Pb.setVisibility(View.GONE);
                }
                tempoDateAdapterV2.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<TempoHistory> call, Throwable t) {

            }
        });

    }
}

