/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Update news every 30 seconds
    private final int FETCH_INTERVAL = 30000;

    // Used to update the news every FETCH_INTERVAL seconds
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        // Fetch news every FETCH_INTERVAL seconds
        @Override
        public void run() {
            searchNews();

            mHandler.postDelayed(mRunnable, FETCH_INTERVAL);
        }
    };

    // Used to keep track of the previous search results
    private String mPreviousSearchResults = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);


        // Find the search button set a click listener on that View
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                searchNews();
            }
        });

        // Perform the initial search
        searchNews();

        // Update data every X seconds
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, FETCH_INTERVAL);
    }

    /**
     * Fetch the latest news
     */
    private void searchNews() {
        DownloadWebPageTask task = new DownloadWebPageTask();

        task.execute(new String[]{getString(R.string.search_url)});
    }

    /**
     * This internal class executes the search asynchronously
     */
    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            for (String url : urls) {
                HttpURLConnection connection = null;
                try {
                    URL u = new URL(url);
                    connection = (HttpURLConnection) u.openConnection();
                    connection.setRequestMethod(getString(R.string.param_connection_request_method));
                    connection.setRequestProperty(
                            getString(R.string.param_connection_request_property_1),
                            getString(R.string.param_connection_request_property_2));
                    connection.setUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.connect();
                    int status = connection.getResponseCode();

                    switch (status) {
                        case 200:
                        case 201:
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                stringBuilder.append(line + "\n");
                            }
                            bufferedReader.close();
                            return stringBuilder.toString();
                    }
                } catch (MalformedURLException ex) {
                    Log.v(MainActivity.class.getSimpleName(), ex.toString());
                } catch (IOException ex) {
                    Log.v(MainActivity.class.getSimpleName(), ex.toString());
                } finally {
                    if (connection != null) {
                        try {
                            connection.disconnect();
                        } catch (Exception ex) {
                            Log.v(MainActivity.class.getSimpleName(), ex.toString());
                        }
                    }
                }
            }

            return response;
        }

        /**
         * Open an intent with the search results or show a Toast if no results found
         * @param searchResults Search results
         */
        @Override
        protected void onPostExecute(String searchResults) {
            ArrayList<Result> results = new ArrayList<Result>();

            // if we have receive a 'valid' response
            if (searchResults != null && searchResults.length() > 4) {
                // Only update if the searchResults are different to the previous one
                if (searchResults.compareTo(mPreviousSearchResults) != 0) {
                    mPreviousSearchResults = searchResults;

                    // Fragment to be load with the date
                    ResultsFragment resultsFragment = new ResultsFragment();

                    // Pass data to the fragment
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.param_results), searchResults);
                    resultsFragment.setArguments(bundle);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.results_container, resultsFragment);
                    fragmentTransaction.commit();
                }
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.no_results), Toast.LENGTH_SHORT).show();
            }
        }
    }
}



