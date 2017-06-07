package com.tliu.castlehill;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;


public class MarkerClusterItem implements ClusterItem {
    private Marker marker;
    public MarkerClusterItem(Marker marker) {
        this.marker = marker;
    }
    @Override
    public LatLng getPosition() {
        return marker.getPosition();
    }

    @Override
    public String getTitle() {
        return marker.getTitle();
    }

    @Override
    public String getSnippet() {
        return marker.getSnippet();
    }
}
