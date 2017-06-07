package com.tliu.castlehill;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private List<BoulderProblem> problems;
    private String[] colorMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colorMap = new String[6];
        colorMap[0] = "#1D05BD";
        colorMap[1] = "#4A219B";
        colorMap[2] = "#773D79";
        colorMap[3] = "#A45A57";
        colorMap[4] = "#D17635";
        colorMap[5] = "#FF9314";

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setInfoWindowAdapter(new BoulderInfoWindow(getLayoutInflater()));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        problems = CSVParser.parseCSV(getResources().openRawResource(R.raw.climbs));

        for (BoulderProblem p : problems) {
            Marker m = map.addMarker(new MarkerOptions().position(p.getCoordinates()));
            m.setTitle(p.getName());
            m.setSnippet(p.getDescription());
            m.setTag(p);

            if (!p.getArea().equals("Wuthering Heights")) {
                m.setVisible(false);
                continue;
            }

            IconGenerator ig = new IconGenerator(this);
            ig.setStyle(IconGenerator.STYLE_BLUE);
            ig.setColor(Color.parseColor(colorMap[p.getStars()]));
            ig.setContentPadding(0, 0, 0, 0);
            Bitmap b = ig.makeIcon(p.getGrade());
            m.setIcon(BitmapDescriptorFactory.fromBitmap(b));
            p.setMarker(m);
        }


        enableMyLocation();
    }





    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (map != null) {
            // Access to the location has been granted to the app.
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
        }
    }
}
