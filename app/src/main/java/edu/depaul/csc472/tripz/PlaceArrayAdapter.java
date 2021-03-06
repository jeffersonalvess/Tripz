package edu.depaul.csc472.tripz;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Miller on 21/11/2015.
 */
public class PlaceArrayAdapter
        extends ArrayAdapter<PlaceArrayAdapter.PlaceAutocomplete> implements Filterable {
    private static final String TAG = "PlaceArrayAdapter";
    private GoogleApiClient mGoogleApiClient;
    //private AutocompleteFilter mPlaceFilter;
    private ArrayList<Integer> mPlaceFilter;
    private LatLngBounds mBounds;
    private ArrayList<PlaceAutocomplete> mResultList;
    private AutocompleteFilter real_filter;

    /**
     * Constructor
     *
     * @param context  Context
     * @param resource Layout resource
     * @param bounds   Used to specify the search bounds
     * @param filter   Used to specify place types
     */
    public PlaceArrayAdapter(Context context, int resource, LatLngBounds bounds,
                             ArrayList<Integer> filter) {
                             //AutocompleteFilter filter) {
        super(context, resource);
        mBounds = bounds;
        mPlaceFilter = filter;

        ArrayList<Integer> filterTypes = new ArrayList<Integer>();

        for(int i = 0; i < filter.size(); i++){
            if(filter.get(i).equals(Place.TYPE_GEOCODE) || filter.get(i).equals(Place.TYPE_ESTABLISHMENT)){
                filterTypes.add(filter.get(i));
                filter.remove(i);
                i--;
            }
        }

        real_filter = null;

        if(filterTypes.size() > 0)
            real_filter = AutocompleteFilter.create(filterTypes);
    }

    public void setmBounds(LatLngBounds mBounds){this.mBounds = mBounds;}

    public void setmPlaceFilter(ArrayList<Integer> mPlaceFilter){this.mPlaceFilter = mPlaceFilter;}

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            mGoogleApiClient = null;
        } else {
            mGoogleApiClient = googleApiClient;
        }
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    private ArrayList<PlaceAutocomplete> getPredictions(CharSequence constraint) {
        if (mGoogleApiClient != null) {
            Log.i(TAG, "Executing autocomplete query for: " + constraint);
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    //mBounds, mPlaceFilter);
                                    mBounds, real_filter);
            // Wait for predictions, set the timeout.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();

            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error: " + status.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting place predictions: " + status
                        .toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");
            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();

//                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
//                            prediction.getDescription()));

                if(mPlaceFilter.size() > 0) {
                    List<Integer> place_types = prediction.getPlaceTypes();

                    int match = 0;


                    for (int i = 0; match == 0 && i < mPlaceFilter.size(); i++) {
                        Log.i("Teste Filtro: ", mPlaceFilter.get(i).toString());
                        for (int j = 0; match == 0 && j < place_types.size(); j++) {
                            Log.i("Teste Place: ", place_types.get(j).toString());
                            if(mPlaceFilter.get(i).equals(place_types.get(j))) {
                                Log.i("Teste Pegou: ", "PEGOU!");
                                match = 1;
                            }
                        }
                    }

                    if (match == 1) {
                        resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                                prediction.getDescription()));
                    }
                    else{

                    }
                }
                else{
                    resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                            prediction.getDescription()));
                }

            }
            // Buffer release
            autocompletePredictions.release();
            return resultList;
        }
        Log.e(TAG, "Google API client is not connected.");
        return null;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    // Query the autocomplete API for the entered constraint
                    mResultList = getPredictions(constraint);
                    if (mResultList != null) {
                        // Results
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }
}