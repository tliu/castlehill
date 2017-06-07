package com.tliu.castlehill;




import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public static List<BoulderProblem> parseCSV(InputStream is) {
        List<BoulderProblem> boulders = new ArrayList<BoulderProblem>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            // discard header
            br.readLine();

//|3112|,|Flock Hill|,|Donkey Kong|,|V8|,|2|,|-43.186320064218|,|171.747859418390|,|[DT,2017,Compression,Span,Jugs]|,|Do the start of Gibbon then do some big cool moves out left and up.|
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|,\\|");
                int nodeNumber = Integer.parseInt(p[0].replace("|", ""));
                String area = p[1].replace("|", "");
                String name = p[2].replace("|", "");
                String strgrade = p[3].replace("|", "");
                int grade = Grades.stringToGrade(strgrade);
                int stars = Integer.parseInt(p[4].replace("|", ""));
                double lat = Double.parseDouble(p[5].replace("|", ""));
                double lng = Double.parseDouble(p[6].replace("|", ""));
                LatLng coordinates = new LatLng(lat, lng);
                List<String> tags = new ArrayList<>();
                String[] tagList = p[7].replace("|", "").replace("]","").replace("[","").split(",");
                for (String s : tagList) {
                    tags.add(s);
                }

                String desc = "";
                if (p.length == 9) {
                    desc = p[8].replace("|", "");
                }

                BoulderProblem boulder = new BoulderProblem(area, name, grade, nodeNumber, stars, coordinates, tags, desc);
                boulders.add(boulder);

            }
            br.close();
        }
        catch (IOException e) {
        }
        return boulders;
    }
}
