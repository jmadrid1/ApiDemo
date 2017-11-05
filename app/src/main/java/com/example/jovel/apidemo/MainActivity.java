package com.example.jovel.apidemo;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jovel.apidemo.config.Config;
import com.example.jovel.apidemo.config.EndpointApi;
import com.example.jovel.apidemo.config.Locations;
import com.example.jovel.apidemo.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button mLogin;
    private TextView mName;
    private TextView mLatitude;
    private TextView mLongitude;
    private Spinner mLocation;
    private Retrofit mRetro;
    private EndpointApi mEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogin = (Button)findViewById(R.id.btn_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mName = (TextView)findViewById(R.id.text_name);
        mName.setVisibility(View.INVISIBLE);

        mLongitude = (TextView)findViewById(R.id.text_longitude);
        mLongitude.setVisibility(View.INVISIBLE);

        mLatitude = (TextView)findViewById(R.id.text_latitude);
        mLatitude.setVisibility(View.INVISIBLE);

        mLocation = (Spinner)findViewById(R.id.spinner_location);
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_location, R.layout.support_simple_spinner_dropdown_item);
        locationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mLocation.setAdapter(locationAdapter);
        mLocation.setVisibility(View.INVISIBLE);

        final String token = getIntent().getStringExtra("token");
        if(token != null){

            showViews();

            mLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    changeLocation(i, token);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    /*
    Switches over to the WebActivity
     */
    public void signIn() {

        Intent intent = new Intent(MainActivity.this, WebActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(0, 0);

        startActivity(intent);
    }

    /*
    Sends the HTTP request to the Instagram API
    endpoint and uses the response to populate
    textviews based on location
     */
    public void sendRequest(int location, String token) {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(Config.INSTA_ENDPOINT_URL)
                .addConverterFactory(GsonConverterFactory.create());

        mRetro = builder.build();
        mEndpoint = mRetro.create(EndpointApi.class);

        Call<ApiResponse> call = mEndpoint.getLocations(location, token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                ApiResponse apiResponse = response.body();

                mName.setText("Name:   " + apiResponse.data.getName());
                mLongitude.setText("Longitude:  " + String.valueOf(apiResponse.data.getLongitude()));
                mLatitude.setText("Latitude:   " + String.valueOf(apiResponse.data.getLatitude()));
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                mName.setText("Name:   " + "N/A");
                mLongitude.setText("Longitude:  " + "N/A");
                mLatitude.setText("Latitude:   " + "N/A");
            }
        });
    }

    /*
    This changes the location ID that is
    used by the Retrofit interface
     */
    private void changeLocation(int position, String token){

        int location = 0;

        switch (position){

            case 0:
                location = Locations.MAMMOTH;
                sendRequest(location, token);
                break;

            case 1:
                location = Locations.SEOUL;
                sendRequest(location, token);
                break;

            case 2:
                location = Locations.PORTLAND;
                sendRequest(location, token);
                break;
            }
    }

    /*
    Presents the needed views once token is acquired
     */
    private void showViews(){
        mLogin.setVisibility(View.INVISIBLE);
        mLocation.setVisibility(View.VISIBLE);
        mName.setVisibility(View.VISIBLE);
        mLatitude.setVisibility(View.VISIBLE);
        mLongitude.setVisibility(View.VISIBLE);
    }

}