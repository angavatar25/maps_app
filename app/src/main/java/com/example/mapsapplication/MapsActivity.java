package com.example.mapsapplication;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button go, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button go = (Button)findViewById(R.id.btn_go);
        go.setOnClickListener(op);

        Button search = (Button)findViewById(R.id.btn_search);
        search.setOnClickListener(op_search);
    }

    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_go:
                    sembunyikanKeyBoard(view);
                    gotoLokasi();
                break;
            }
        }
    };

    View.OnClickListener op_search = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_search:
                    goCari(); break;
            }
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ITS = new LatLng(-7.28,112.79);
        mMap.addMarker(new MarkerOptions().position(ITS).title("Marker in ITS"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS,15));
    }

    private void gotoPeta(Double lattitude, Double longitude, float z) {
        LatLng Lokasibaru = new LatLng(lattitude,longitude);
        mMap.addMarker(new MarkerOptions().position(Lokasibaru).title("Marker in  " +lattitude +":" +longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Lokasibaru, z));
    }

    private void gotoLokasi() {
        EditText latitude = (EditText)findViewById(R.id.et_lattitude);
        EditText longitude = (EditText)findViewById(R.id.et_longitude);
        EditText et_zoom = (EditText)findViewById(R.id.et_zoom);

        Double doubleLat = Double.parseDouble(latitude.getText().toString());
        Double doubleLong = Double.parseDouble(longitude.getText().toString());
        Float doubleZoom = Float.parseFloat(et_zoom.getText().toString());

        Toast.makeText(this,"Move to latitude :" +doubleLat + "longitude : " +doubleLong, Toast.LENGTH_LONG).show();
        gotoPeta(doubleLat,doubleLong,doubleZoom);
    }

    private void sembunyikanKeyBoard(View v) {
        InputMethodManager a = (InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private void goCari() {
        EditText tempat = (EditText)findViewById(R.id.et_search);
        Geocoder geo = new Geocoder(getBaseContext());
        try {
            List<Address> daftar = null;
            try {
                daftar = geo.getFromLocationName(tempat.getText().toString(),1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address alamat = daftar.get(0);

            String nemuAlamat =  alamat.getAddressLine(0);
            Double lintang = alamat.getLatitude();
            Double bujur = alamat.getLongitude();

            Toast.makeText(getBaseContext(),"Ketemu " + nemuAlamat,Toast.LENGTH_LONG).show();
            EditText zoom = (EditText)findViewById(R.id.et_zoom);
            Float doubleZoom = Float.parseFloat(zoom.getText().toString());
            Toast.makeText(this,"Move to "+ nemuAlamat +" Lat:" +
                    lintang + " Long:" +bujur,Toast.LENGTH_LONG).show();
            gotoPeta(lintang,bujur,doubleZoom);

            EditText lat = (EditText) findViewById(R.id.et_lattitude);
            EditText lng = (EditText) findViewById(R.id.et_longitude);

            lat.setText(lintang.toString());
            lng.setText(bujur.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
