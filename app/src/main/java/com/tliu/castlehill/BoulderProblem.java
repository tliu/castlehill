package com.tliu.castlehill;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

//|1333|,|Dry Valley|,|Wisp|,|V9|,|1|,|-43.196522017900|,|171.751515269000|,|[DT,2003,Dyno]|
//|3112|,|Flock Hill|,|Donkey Kong|,|V8|,|2|,|-43.186320064218|,|171.747859418390|,|[DT,2017,Compression,Span,Jugs]|,|Do the start of Gibbon then do some big cool moves out left and up.|
public class BoulderProblem {
    private String area;
    private String name;
    private int grade;
    private int nodeNumber;
    private int stars;
    private Marker marker;
    private LatLng coordinates;
    private List<String> tags;
    private String description;

    public BoulderProblem(String area, String name, int grade, int nodeNumber, int stars, LatLng coordinates, List<String> tags, String description) {
        this.area = area;
        this.name = name;
        this.grade = grade;
        this.nodeNumber = nodeNumber;
        this.stars = stars;
        this.marker = marker;
        this.coordinates = coordinates;
        this.tags = tags;
        this.description = description;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
