package compraapp.appbuyers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import compraapp.appbuyers.entities.Buyer;
import compraapp.appbuyers.request.Utils;

public class EditUser extends AppCompatActivity implements OnMapReadyCallback{

    EditText name;
    EditText phone;
    EditText email;
    EditText address;
    Button btnAcceptEditUser;
    double longitudeUser, latitudeUser;

    LocationManager locationManager;
    double longitudeNetwork, latitudeNetwork;
    TextView longitudeValueNetwork, latitudeValueNetwork;
    Marker marker;
    GoogleMap googleMap;
    boolean map_started;
    boolean map_moved;

    boolean config_received;
    boolean error_comunication;

    private Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        this.name = (EditText) findViewById(R.id.editName);
        this.phone = (EditText) findViewById(R.id.editPhone);
        this.email = (EditText) findViewById(R.id.editEmail);
        this.address = (EditText) findViewById(R.id.editAddress);

        this.btnAcceptEditUser = (Button) findViewById(R.id.btnSaveProfile);
        btnAcceptEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonObjRequest();
            }
        });

        getProfile();

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startGPS();

        customHandler.postDelayed(updateTimerThread, 3000);

    }


    private void getProfile(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Utils.SERVER_URL + "/buyer/"+Utils.buyer_id;
        StringRequest jsonObjReq = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        useResponseGet(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMsg("onErrorResponse: " + error.getMessage());
                /*refreshListFromDataBase();*/
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(jsonObjReq);
    }



    private void useResponseGet(String response){
        try {
            //JSONArray json = new JSONArray(response);
            Gson gson = new GsonBuilder().create();
            Buyer object = gson.fromJson(response.toString(), Buyer.class);

            this.name.setText(object.getName());
            this.phone.setText(object.getPhone());
            this.email.setText(object.getEmail());
            this.address.setText(object.getAddress());
            this.longitudeUser = object.getLongitud();
            this.latitudeUser = object.getLatitud();
            this.config_received = true;
            this.error_comunication = false;

        } catch (Exception e) {
            this.error_comunication = true;
            this.config_received = false;
            e.printStackTrace();
            showErrorMsg("Error request: " + e.getMessage());
        }
    }

    private void useResponseGetUser(String response){
        try {
            //JSONArray json = new JSONArray(response);
            Gson gson = new GsonBuilder().create();
            Buyer buyerObject = gson.fromJson(response.toString(), Buyer.class);

            this.name.setText(buyerObject.getName());
            this.email.setText(buyerObject.getEmail());
            this.phone.setText(buyerObject.getPhone());
            this.address.setText(buyerObject.getAddress());

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("Error request: " + e.getMessage());
        }
    }



    private void startGPS(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1 * 1000,
                500,
                locationListenerNetwork
        );
    }


    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // moverMapa(longitudeNetwork, latitudeNetwork);
                    //locationManager.removeUpdates(locationListenerNetwork);
                   // Toast.makeText(EditUser.this, longitudeNetwork + "-" + latitudeNetwork, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {

        }
        @Override
        public void onProviderDisabled(String s) {

        }
    };


    // Include the OnCreate() method here too, as described above.
    @Override
    public void onMapReady(GoogleMap gMap) {
        map_started = true;
        googleMap = gMap;

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(marker!=null){
                    setAddress(latLng);
                }
            }
        });
    }

    private void setAddress(LatLng latLng){
        latitudeUser = latLng.latitude;
        longitudeUser = latLng.longitude;

        marker.setPosition(latLng);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String googleAddress = addresses.get(0).getAddressLine(0);
            address.setText(googleAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moverMapa(double longitud,double latitud){
        LatLng posicion = new LatLng(latitud,longitud);
        if(googleMap!=null){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion,15));
        }
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            if(!mapMoveCheck())
                customHandler.postDelayed(this, 1000);
            //Toast.makeText(EditUser.this, "Timer", Toast.LENGTH_SHORT).show();
        }
    };


    private boolean mapMoveCheck(){
        if(map_started){
            if(config_received){
                if(latitudeUser!=0){
                    moverMapa(longitudeUser, latitudeUser);

                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitudeUser, longitudeUser))
                            .title("Tu dirección").visible(true));
                    marker.showInfoWindow();
                    return true;
                } else if (longitudeNetwork != 0) {
                    longitudeUser =longitudeNetwork;
                    latitudeUser = latitudeNetwork;

                    moverMapa(longitudeNetwork, latitudeNetwork);

                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitudeNetwork, longitudeNetwork))
                            .title("Click en el mapa para ubicar la dirección").visible(true));
                    marker.showInfoWindow();

                    return true;
                }
            }
        }

        return false;
    }


    private void makeJsonObjRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("Name", name.getText().toString());
        postParam.put("Phone", phone.getText().toString());
        postParam.put("Email", email.getText().toString());
        postParam.put("Address",address.getText().toString());
        postParam.put("Longitud",longitudeUser+"");
        postParam.put("Latitud",latitudeUser+"");

        String url = Utils.SERVER_URL + "/buyer/" + Utils.buyer_id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        useResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMsg(error.getMessage());
            }
        })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(jsonObjReq);

    }

    private void useResponse(JSONObject response){
        try {
            int id = response.getInt("Id");
            if(id>0) {

                setResult(RESULT_OK, null);
                showOkMsg("Perfil guardado  correctamente");
                finish();
            }
            else{
                showErrorMsg("Error al guardar en el servidor.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMsg("Error json: " + e.getMessage());
        }
    }

    private void showErrorMsg(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void showOkMsg(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



}
