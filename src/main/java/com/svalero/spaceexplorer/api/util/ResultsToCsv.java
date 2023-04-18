package com.svalero.spaceexplorer.api.util;

import com.svalero.spaceexplorer.api.model.LaunchInfo.Result;

import java.io.*;
import java.util.List;

public class ResultsToCsv {
    private static final String CSV_SEPARATOR = ";";

    public static void writeToCSV(List<Result> resultList, File outputfile) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile), "UTF-8"));
            StringBuilder oneLine = new StringBuilder();
            oneLine.append("Name" + CSV_SEPARATOR + "Launch service provider" + CSV_SEPARATOR + "Mission start" + CSV_SEPARATOR + "Rocket name" +
                    CSV_SEPARATOR + "Mission name" + CSV_SEPARATOR + "Pad name" + CSV_SEPARATOR + "Status");
            bw.write(oneLine.toString());
            bw.newLine();
            for (Result result : resultList) {
                oneLine.delete(0, oneLine.length());
                oneLine.append(result.getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(result.getLaunch_service_provider().getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(result.getWindow_start());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(result.getRocket().getConfiguration().getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(result.getMission().getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(result.getPad().getName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(result.getStatus().getDescription());
                oneLine.append(CSV_SEPARATOR);
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException ignored) {
        }
    }
}
