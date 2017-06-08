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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
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
    private Dialog areaFilterDialog;
    private Dialog gradeFilterDialog;
    private Dialog starFilterDialog;
    private Dialog nameFilterDialog;


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

        areaFilterDialog.show();

    }
    private void makeAreaFilter() {
        areaFilterDialog = new Dialog(this);

        areaFilterDialog.setContentView(R.layout.filter_dialog);

        TextView tv = (TextView)areaFilterDialog.findViewById(R.id.titleText);
        tv.setText("Filter by Area");
        LinearLayout lv = (LinearLayout) areaFilterDialog.findViewById(R.id.areaList);

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

    }

    private void updateMarkers() {
        for (BoulderProblem b : problems) {
            boolean show = true;
            show = filters.getAreas().get(b.getArea()) && show;
            show = filters.getGrades().get(b.getGrade()) && show;
            show = filters.getStars().get(b.getStars()) && show;
            show = b.getName().replace("\\s+", "").toLowerCase().contains(filters.getNameSearch().toLowerCase()) && show;
            b.getMarker().setVisible(show);
        }
    }
    private void showGradeFilter() {
        gradeFilterDialog.show();
    }

    private void showNameFilter() {
        nameFilterDialog.show();
    }
    private void makeNameFilter() {
        nameFilterDialog = new Dialog(this);

        nameFilterDialog.setContentView(R.layout.filter_dialog);

        TextView tv = (TextView)nameFilterDialog.findViewById(R.id.titleText);
        tv.setText("Filter by Name");
        LinearLayout lv = (LinearLayout) nameFilterDialog.findViewById(R.id.areaList);

        EditText et = new EditText(this);

        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                filters.setNameSearch(s.toString());
                updateMarkers();
            }
        });
        lv.addView(et);

    }
    private void makeGradeFilter() {

        gradeFilterDialog = new Dialog(this);

        gradeFilterDialog.setContentView(R.layout.filter_dialog);

        TextView tv = (TextView)gradeFilterDialog.findViewById(R.id.titleText);
        tv.setText("Filter by Grade");
        LinearLayout lv = (LinearLayout) gradeFilterDialog.findViewById(R.id.areaList);

        for (Integer grade : filters.getGrades().keySet()) {
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


    }
    private void showStarFilter() {
        starFilterDialog.show();

    }


    private void filterText(String txt) {
        for (BoulderProblem b : problems) {
            b.getMarker().setVisible(b.getName().replace("\\s+","").toLowerCase().contains(txt.toLowerCase()));
        }
    }

    private void makeStarFilter() {

        starFilterDialog = new Dialog(this);

        starFilterDialog.setContentView(R.layout.filter_dialog);

        TextView tv = (TextView)starFilterDialog.findViewById(R.id.titleText);
        tv.setText("Filter by Stars");
        LinearLayout lv = (LinearLayout) starFilterDialog.findViewById(R.id.areaList);

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
        String[] filterList = {"Areas", "Grades", "Stars", "Name"};
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
                        showNameFilter();
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

        makeAreaFilter();
        makeGradeFilter();
        makeStarFilter();
        makeNameFilter();
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
