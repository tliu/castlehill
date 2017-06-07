package com.tliu.castlehill;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap map;
    private List<BoulderProblem> problems;
    private String[] colorMap;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private FilterState filters;


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

    private void showAreaFilter() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.filter_dialog);

        TextView tv = (TextView)dialog.findViewById(R.id.titleText);
        tv.setText("Filter by Area");
        LinearLayout lv = (LinearLayout) dialog.findViewById(R.id.areaList);

        for (String area : filters.getAreas().keySet()) {
            Switch sw = new Switch(this);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String txt = (String)buttonView.getText();
                    filters.getAreas().put(txt, isChecked);
                    updateMarkers();
                }
            });
            lv.addView(sw);
            sw.setText(area);
            sw.setChecked(filters.getAreas().get(area));
        }

        dialog.show();
    }

    private void updateMarkers() {
        for (BoulderProblem b : problems) {
            boolean show = true;
            show = filters.getAreas().get(b.getArea()) && show;
            show = filters.getGrades().get(b.getGrade()) && show;
            show = filters.getStars().get(b.getStars()) && show;
            b.getMarker().setVisible(show);
        }
    }
    private void showGradeFilter() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.filter_dialog);

        TextView tv = (TextView)dialog.findViewById(R.id.titleText);
        tv.setText("Filter by Grade");
        LinearLayout lv = (LinearLayout) dialog.findViewById(R.id.areaList);

        for (Integer grade : filters.getGrades().keySet()) {
            Log.i("GRADE: ", "" + grade);
            Switch sw = new Switch(this);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int txt = Grades.stringToGrade((String)buttonView.getText());
                    filters.getGrades().put(txt, isChecked);
                    updateMarkers();
                }
            });
            lv.addView(sw);
            sw.setText(Grades.gradeToString(grade));
            sw.setChecked(filters.getGrades().get(grade));
        }

        dialog.show();
    }
    private void showStarFilter() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.filter_dialog);

        TextView tv = (TextView)dialog.findViewById(R.id.titleText);
        tv.setText("Filter by Stars");
        LinearLayout lv = (LinearLayout) dialog.findViewById(R.id.areaList);

        for (Integer stars : filters.getStars().keySet()) {
            Switch sw = new Switch(this);
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String txt = (String)buttonView.getText();
                    filters.getStars().put(Integer.parseInt(txt), isChecked);
                    updateMarkers();
                }
            });
            lv.addView(sw);
            sw.setText("" + stars);
            sw.setChecked(filters.getStars().get(stars));
        }

        dialog.show();

    }
    private void resetFilters() {
        for (String key : filters.getAreas().keySet()) {
            filters.getAreas().put(key, true);
        }
        for (Integer key : filters.getGrades().keySet()) {
            filters.getGrades().put(key, true);
        }
        for (Integer key : filters.getStars().keySet()) {
            filters.getStars().put(key, true);
        }
        updateMarkers();
    }
    private void setUpMenu() {
        String[] filterList = {"Areas", "Grades", "Stars", "Reset All"};
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterList);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        showAreaFilter();
                        break;
                    case 1:
                        showGradeFilter();
                        break;
                    case 2:
                        showStarFilter();
                        break;
                    case 3:
                        resetFilters();
                        break;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-43.207696, 171.735368), 13.0f));
        UiSettings ui = map.getUiSettings();
        ui.setCompassEnabled(true);
        ui.setZoomControlsEnabled(true);
        ui.setMapToolbarEnabled(false);


        map.setInfoWindowAdapter(new BoulderInfoWindow(getLayoutInflater()));
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        problems = CSVParser.parseCSV(getResources().openRawResource(R.raw.climbs));
        filters = new FilterState(problems);

        for (BoulderProblem p : problems) {
            Marker m = map.addMarker(new MarkerOptions().position(p.getCoordinates()));
            m.setTitle(p.getName());
            m.setSnippet(p.getDescription());
            m.setTag(p);

            IconGenerator ig = new IconGenerator(this);
            ig.setStyle(IconGenerator.STYLE_BLUE);
            ig.setColor(Color.parseColor(colorMap[p.getStars()]));
            ig.setContentPadding(0, 0, 0, 0);
            Bitmap b = ig.makeIcon(Grades.gradeToString(p.getGrade()));
            m.setIcon(BitmapDescriptorFactory.fromBitmap(b));
            p.setMarker(m);
        }


        setUpMenu();
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
