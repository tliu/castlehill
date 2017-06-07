package com.tliu.castlehill;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

class BoulderInfoWindow implements InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;

    BoulderInfoWindow(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.infowindow, null);
        }


        BoulderProblem p = (BoulderProblem)marker.getTag();


        TextView tv=(TextView)popup.findViewById(R.id.boulderDesc);
        tv.setText(marker.getSnippet());
        tv=(TextView)popup.findViewById(R.id.boulderTitle);
        tv.setText(marker.getTitle() + "(" + Grades.gradeToString(p.getGrade()) + ")");
        tv=(TextView)popup.findViewById(R.id.tagsView);

        String tags = "";
        for (String s : p.getTags()) {
            tags = tags + s + ", ";
        }
        if (tags.length() > 0)
            tags = tags.substring(0, tags.length() - 2);
        tv.setText(tags);

        RatingBar rb = (RatingBar)popup.findViewById(R.id.ratingBar);
        rb.setRating((float)p.getStars());
        return(popup);
    }
}
