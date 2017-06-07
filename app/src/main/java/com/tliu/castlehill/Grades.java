package com.tliu.castlehill;

import android.util.Log;

/**
 * Created by tliu on 6/8/17.
 */

public class Grades {
    public static String gradeToString(int grade) {
        String suff = "" + grade;
        if (grade == -2) suff = "E";
        if (grade == -1) suff = "M";
        if (grade == 16) suff = "1?";
        if (grade == 17) suff = "?";
        return "V" + suff;
    }
    public static int stringToGrade(String grade) {
        String str = grade.substring(1);
        int out = 0;
        try {
            out = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            if (str.equals("1?")) return 16;
            if (str.equals("?")) return 17;
            if (str.equals("M")) return -1;
            if (str.equals("E")) return -2;
        }
        return out;
    }
}
