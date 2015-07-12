package taxitouchzgz.example.com.taxitouchzgzv2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.app.ActionBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import taxitouchzgz.example.com.Interfaces.TaxiInterface;


public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    public static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean newAcces = true;    // False after open the app
    public static ArrayList<Marker> points;
    public static ArrayList<Marker> pointsTaxi;
    private boolean taxiPressed;
    private String username;

    private ProgressDialog pDialog;

    private String[] mNeigh;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;

    private LatLng currentPosition;
    private EditText etLat, etLon;
    private TextView tvAddress;
    private CheckBox cbPos;
    private Spinner spDest;
    private TimePicker timePicker;
    private Button btAccept;
    private double lat, lon;
    Geocoder geo;
    List<Address> addresses = null;
    private int myPosition;
    private String username2;

    public static boolean notification = false;
    public static String touchedBy;
    private MenuItem playMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mMap.setMyLocationEnabled(true);
        mNeigh = getResources().getStringArray(R.array.neighborhoods);

        username = getIntent().getExtras().getString("username");

        // Create Drawer Layout
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        selectItem(0);

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTaxiStops();
        CameraPosition camPos = new CameraPosition.Builder()
                .target(new LatLng(41.65, -0.883333))
                .zoom(11.0f)
                .build();

        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

        mMap.moveCamera(camUpdate);


        // Initialize the map with the user's location
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                if (newAcces) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 14.0f));
                    newAcces = false;
                }
            }
        };

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                infoWindowDialog(marker);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
            mMap.getUiSettings().setZoomGesturesEnabled(true);

            mMap.setOnMapLongClickListener(this);

        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }




    private void setTaxiStops(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://iescities.com:443/IESCities/api")
                .build();
        TaxiInterface service = restAdapter.create(TaxiInterface.class);

        String query = "select * from paradasTaxi";
        TypedInput string = new TypedByteArray("text/plain",query.getBytes());
        service.listTaxiStop(string, new Callback<Response>() {
            public void success(Response result, Response response) {
                //Try to get response body
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();

                try {
                    reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String line;

                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
                String jTaxi = sb.toString().replace("{}", "null");
                try {
                    JSONObject obj = new JSONObject(jTaxi);

                    JSONArray rows = obj.getJSONArray("rows");
                    pointsTaxi = new ArrayList<Marker>();

                    for (int i = 0; i < rows.length(); i++) {
                        String l1 = rows.getJSONObject(i).getString("lng");
                        String l2 = rows.getJSONObject(i).getString("lat");
                        String id = rows.getJSONObject(i).getString("id");
                        String direccion = rows.getJSONObject(i).getString("direccion");

                        float lng = Float.valueOf(l1);
                        float lat = Float.valueOf(l2);

                        pointsTaxi.add(mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .title(direccion)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                ));


                    }

                    taxiPressed = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {

        // Create custom dialog object
        final Dialog dialog = new Dialog(MapsActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_layout);
        // Set dialog title
        dialog.setTitle("Set your meeting point");

        geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());



        //etLat = (EditText) dialog.findViewById(R.id.etLat);
        //etLon = (EditText) dialog.findViewById(R.id.etLon);
        tvAddress = (TextView) dialog.findViewById(R.id.tvAddress);
        btAccept = (Button) dialog.findViewById(R.id.btAcepet);
        cbPos = (CheckBox) dialog.findViewById(R.id.cbPos);
        spDest = (Spinner) dialog.findViewById(R.id.spDest);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.neighborhoods, android.R.layout.simple_spinner_item);
        // Specify the layout to use hwen the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spDest.setAdapter(adapter);
        spDest.setSelection(0);

        timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // set current time into timepicker
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        lat = latLng.latitude;
        lon = latLng.longitude;


        try {
            addresses = geo.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                tvAddress.setText(addresses.get(0).getAddressLine(0));
                //tvAddress.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //etLat.setText(latLng.latitude + "");
        //etLon.setText(latLng.longitude + "");

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date;
                if (timePicker.is24HourView()) {
                    date = (timePicker.getCurrentHour()+12) + "" ;
                }
                else {
                    date = timePicker.getCurrentHour() + "";
                }
                date += ":" + timePicker.getCurrentMinute();

                String address =  addresses.get(0).getAddressLine(0);
                String dest = spDest.getItemAtPosition(spDest.getSelectedItemPosition()).toString();

                new LocationActivity(getApplicationContext()).execute(username, dest, lon+"", lat+"", date);

                dialog.dismiss();

                new PeopleFilter(MapsActivity.this).execute(username, mNeigh[myPosition]);

            }
        });

        cbPos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    //etLat.setText(currentPosition.latitude + "");
                    //etLon.setText(currentPosition.longitude + "");
                    lat = currentPosition.latitude;
                    lon = currentPosition.longitude;
                }
                else {
                    //etLat.setText(latLng.latitude + "");
                    //etLon.setText(latLng.longitude + "");
                    lat = latLng.latitude;
                    lon = latLng.longitude;
                }

                try {
                    addresses = geo.getFromLocation(lat, lon, 1);
                    if (addresses.size() > 0) {
                        tvAddress.setText(addresses.get(0).getAddressLine(0));
                        //tvAddress.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                        //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        dialog.show();
    }


    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }

    }


    private void selectItem(int position) {
        // update the main content by replacing fragments
		/*
		 * Fragment fragment = new PlanetFragment(); Bundle args = new Bundle();
		 * args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		 * fragment.setArguments(args);
		 *
		 * FragmentManager fragmentManager = getFragmentManager();
		 * fragmentManager.beginTransaction().replace(R.id.content_frame,
		 * fragment).commit();
		 */

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        // setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

        myPosition = position;
        new PeopleFilter(this).execute(username, mNeigh[position]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);

        playMenu = menu.findItem(R.id.notification);

        new getNotification(getApplicationContext(), playMenu).execute(username);

        if (notification) {
            playMenu.setIcon(R.drawable.notification_on);
        }
        else {
            playMenu.setIcon(R.drawable.notification_off);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.info) {
            Intent info = new Intent(this,Info.class);
            startActivity(info);
            return true;
        }
        else if (id == R.id.notification) {
            if (notification) {
                notificationDialog();
            }
            else {
            }
        }
        else if (id == R.id.taxi) {
            if (taxiPressed) {
                taxiPressed = false;
                for (Marker m:pointsTaxi) {
                    m.setVisible(false);
                }
            }
            else{
                taxiPressed = true;
                for (Marker m:pointsTaxi) {
                    m.setVisible(true);
                }
            }
        }


        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addDrawerItems() {
        /*String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);*/

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mNeigh));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MapsActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                selectItem(position);
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Choose one...");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void infoWindowDialog(Marker marker) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(MapsActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.info_window_dialog);
        // Set dialog title
        dialog.setTitle("Get taxi");

        Scanner s = new Scanner(marker.getTitle());
        username2 = s.next();

        geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());

        TextView tvQuestion = (TextView) dialog.findViewById(R.id.tvQuestion);
        tvQuestion.setText(tvQuestion.getText() + " " + username2 + "?");

        Button btAccept = (Button) dialog.findViewById(R.id.btAccept);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        Button btChat = (Button) dialog.findViewById(R.id.btChat);

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendRequest(getApplicationContext()).execute(username2, username);
                dialog.dismiss();;
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();;
            }
        });

        btChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChatView.class);
                i.putExtra("sender", username);
                i.putExtra("receiver",username2);
                startActivity(i);
                dialog.dismiss();;
            }
        });

        dialog.show();
    }


    public void notificationDialog() {
        // Create custom dialog object
        final Dialog dialog = new Dialog(MapsActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.notification_dialog);
        // Set dialog title
        dialog.setTitle("Notification");

        geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());

        TextView tvQuestion = (TextView) dialog.findViewById(R.id.tvResponse);
        tvQuestion.setText("You have been touched by " + touchedBy + ". ");

        Button btAccept = (Button) dialog.findViewById(R.id.btAccept);
        Button btChat = (Button) dialog.findViewById(R.id.chatButton);

        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deleteNotification(getApplicationContext()).execute(username);
                playMenu.setIcon(R.drawable.notification_off);
                dialog.dismiss();;
            }
        });

        btChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ChatView.class);
                i.putExtra("sender", username);
                i.putExtra("receiver",touchedBy);
                startActivity(i);
                dialog.dismiss();;
            }
        });

        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

}
