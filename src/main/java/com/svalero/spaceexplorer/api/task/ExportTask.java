package com.svalero.spaceexplorer.api.task;

import com.svalero.spaceexplorer.api.model.LaunchInfo.Result;
import javafx.concurrent.Task;

import java.io.*;
import java.util.List;

public class ExportTask extends Task<Integer> {
    private List<Result> resultList;
    private File file;
    private static final String CSV_SEPARATOR = ";";
    Integer currentIter = 0;


    public ExportTask(List<Result> resultList, File file) {
        this.resultList = resultList;
        this.file = file;
    }

    @Override
    protected Integer call() throws Exception {
        double exportProgress = 0.0;

        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            StringBuilder oneLine = new StringBuilder();
            oneLine.append("Name" + CSV_SEPARATOR + "Launch service provider" + CSV_SEPARATOR + "Mission start" + CSV_SEPARATOR + "Rocket name" +
                    CSV_SEPARATOR + "Mission name" + CSV_SEPARATOR + "Pad name" + CSV_SEPARATOR + "Status");
            bw.write(oneLine.toString());
            bw.newLine();
            for (Result result : resultList) {
                currentIter++;
                exportProgress = ((double) currentIter / resultList.size());
                updateProgress(exportProgress, 1);
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
                Thread.sleep(10);
            }
            updateProgress(1, 1);
            updateMessage("Done!");
            bw.flush();
            bw.close();
        } catch (IOException ignored) {
        }


        return null;
    }
}
