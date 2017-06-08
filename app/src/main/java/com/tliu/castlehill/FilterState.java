package com.tliu.castlehill;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FilterState {


    private Map<String, Boolean> areas;


    private Map<Integer, Boolean> grades;
    private Map<Integer, Boolean> stars;


    private String nameSearch;
    public FilterState(List<BoulderProblem> problems) {
        areas = new TreeMap<>();
        grades = new TreeMap<>();
        stars = new TreeMap<>();
        for (BoulderProblem b : problems) {
            if (!areas.containsKey(b.getArea())) {
                areas.put(b.getArea(), true);
            }
            if (!grades.containsKey(b.getGrade())) {
                grades.put(b.getGrade(), true);
            }
            if (!stars.containsKey(b.getStars())) {
                stars.put(b.getStars(), true);
            }

        }
        nameSearch = "";
    }
    public Map<String, Boolean> getAreas() {
        return areas;
    }

    public void setAreas(Map<String, Boolean> areas) {
        this.areas = areas;
    }


    public Map<Integer, Boolean> getGrades() {
        return grades;
    }

    public void setGrades(Map<Integer, Boolean> grades) {
        this.grades = grades;
    }

    public Map<Integer, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<Integer, Boolean> stars) {
        this.stars = stars;
    }

    public String getNameSearch() {
        return nameSearch;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }
}
