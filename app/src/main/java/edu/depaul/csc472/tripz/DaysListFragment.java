package edu.depaul.csc472.tripz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;

import edu.depaul.csc472.tripz.helper.City;
import edu.depaul.csc472.tripz.helper.DatabaseHelper;
import edu.depaul.csc472.tripz.helper.Day;
import edu.depaul.csc472.tripz.helper.OurDate;

/**
 * Created by jeffersonalvess on 11/22/15.
 */
public class DaysListFragment extends ListFragment {

    /** The serialization (saved instance state) Bundle key representing the activated item position. Only used on tablets. */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /** The fragment's current callback object, which is notified of list item clicks. */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**  The current activated item position. Only used on tablets. */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private DatabaseHelper databaseHelper;
    private int cityID = -1;

    public static ArrayList<Day> DAYS = new ArrayList<>();


    public DaysListFragment() {}


    /** A callback interface that all activities containing this fragment must implement. This mechanism allows activities to be notified of item selections. */
    public interface Callbacks {
        /**  Callback for when an item has been selected. */
        public void onItemSelected(int id, int cityID);
    }

    /** A dummy implementation of the {@link Callbacks} interface that does nothing. Used only when this fragment is not attached to an activity. */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int id, int cityID) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new TripsAdapter(getActivity()));

    }

    @Override
    public void onStart() {
        super.onStart();


        DaysActivity daysActivity = (DaysActivity) getActivity();
        cityID = daysActivity._cityID;

        if(cityID != -1) {
            databaseHelper = new DatabaseHelper(getActivity());
            DAYS = databaseHelper.getDaysByCityId(cityID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the fragment is attached to one) that an item has been selected.
        // mCallbacks.onItemSelected(WineList.WINES.get(position).getName());

        mCallbacks.onItemSelected(DAYS.get(position).getId(), DAYS.get(position).getIndex());
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        //registerForContextMenu(this.getListView());
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {
                Toast.makeText(getActivity(), DAYS.get(arg2).getIndex() + " On long click listener ", Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getListView().getContext());
                builder1.setMessage("Do you want to delete Day " + DAYS.get(arg2).getIndex() + "?");

                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                ArrayList<Place> ps = new ArrayList<Place>();

                                //ds = databaseHelper.getDaysByCityId(CITIES.get(arg2).getId());
                                //for (Day d : ds) {
                                //    databaseHelper.deleteDay(d.getId());
                                //}
                                databaseHelper.deleteDay(DAYS.get(arg2).getId());

                                Toast.makeText(getActivity(), DAYS.get(arg2).getIndex() + " deleted. ", Toast.LENGTH_LONG).show();

                                City cNova = databaseHelper.getCity(cityID);

                                if (cNova.getStart().equals(DAYS.get(arg2).getDate())) {
                                    //ds = city.getStart();
                                    cNova.setStart(cNova.getStart().addDaysOnDate(1));
                                    Log.i("NOVA DATA: ", cNova.getStartString());
                                }
                                if (cNova.getEnd().equals(DAYS.get(arg2).getDate())) {
                                    cNova.setEnd(cNova.getEnd().addDaysOnDate(-1));
                                    Log.i("NOVA DATA: ", cNova.getEndString());
                                }

                                databaseHelper.updateCity(cNova);

                                DAYS.remove(arg2);


                                ((TripsAdapter) getListAdapter()).notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return true;
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /** Turns on activate-on-click mode. When this mode is on, list items will be given the 'activated' state when touched. */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        }
        else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TripsAdapter) getListAdapter()).notifyDataSetChanged();
    }

    ///// Callback from WineDetailFragment. For two-pane layout
    public void onItemChanged() {
        ((TripsAdapter) getListAdapter()).notifyDataSetChanged();
    }

    static class TripsAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        TripsAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() { return DAYS.size();}

        @Override
        public Object getItem(int position) {return DAYS.get(position);}

        @Override
        public long getItemId(int position) {return position;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            View row = convertView;

            if (row == null) {
                row = inflater.inflate(R.layout.list_item_content, parent, false);
                viewHolder = new ViewHolder();

                viewHolder.icon = (ImageView) row.findViewById(R.id.imgIcon);
                viewHolder.title = (TextView) row.findViewById(R.id.txtTitle);
                viewHolder.line1 = (TextView) row.findViewById(R.id.txtLine1);
                viewHolder.line2 = (TextView) row.findViewById(R.id.txtLine2);
                row.setTag(viewHolder);
            }
            else
                viewHolder = (ViewHolder) row.getTag();

            Day c = DAYS.get(position);OurDate d = new OurDate();
            d.setDate("1992/02/28");

            viewHolder.icon.setImageResource(android.R.drawable.ic_menu_today);
            viewHolder.title.setText("Day " + c.getIndex());
            viewHolder.line2.setVisibility(View.GONE);
            viewHolder.line1.setText(c.getDate().getAmericanDate());

            return row;
        }

        static class ViewHolder {
            ImageView icon;
            TextView title;
            TextView line1;
            TextView line2;
        }


    }
}

