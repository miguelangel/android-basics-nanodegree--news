package com.example.android.news;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * {@link Fragment} that displays a list of results.
 */
public class ResultsFragment extends Fragment {
    public ResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.results_list, container, false);

        // Get the results in json
        String json = getArguments().getString(getString(R.string.param_results));

        // Create a list of results
        final ArrayList<Result> results = parseResults(json);

        // Create an {@link ResultAdapter}, whose data source is a list of {@link Result}s. The
        // adapter knows how to create list items for each item in the list.
        ResultAdapter adapter = new ResultAdapter(getActivity(), results);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // results_list layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link ResultAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Result} in the list.
        listView.setAdapter(adapter);

        // Set a click listener to open the link in a browser when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the {@link Result} object at the given position the user clicked on
                // and opens a web browser with the selected news url
                Result result = results.get(position);

                Intent browser = new Intent(Intent.ACTION_VIEW);
                browser.setData(Uri.parse(result.getWebUrl()));
                startActivity(browser);
            }
        });

        return rootView;
    }

    /**
     * @param jsonSearchResponse Search result in json format
     *
     * @return a list of Result object based on the jsonSearchResponse
     */
    private ArrayList<Result> parseResults(String jsonSearchResponse) {
        ArrayList<Result> results = new ArrayList<Result>();
        try {
            JSONObject jsonRootObject = null;

            jsonRootObject = new JSONObject(jsonSearchResponse);

            // Iterate the results in JSON format and create an ArrauList of Result objects

            JSONObject response = jsonRootObject.optJSONObject(getString(R.string.json_response));
            JSONArray resultsJson = response.optJSONArray(getString(R.string.json_results));


            for(int i=0; i < resultsJson.length(); i++){
                try {

                    JSONObject result = resultsJson.getJSONObject(i);

                    String id = result.optString(getString(R.string.json_id)).trim();
                    String webTitle = result.optString(getString(R.string.json_web_title)).trim();
                    String sectionName = result.optString(getString(R.string.json_section_name)).trim();
                    String webPublicationDate = result.optString(getString(R.string.json_web_publication_date)).trim();
                    String webUrl = result.optString(getString(R.string.json_web_url)).trim();

                    results.add(new Result(id, webTitle, sectionName, webPublicationDate, webUrl));
                } catch (JSONException e) {
                    Log.e(MainActivity.class.getSimpleName(), e.toString());
                }
            }
        } catch (JSONException e) {
            Log.e(MainActivity.class.getSimpleName(), e.toString());
        }

        return results;
    }
}