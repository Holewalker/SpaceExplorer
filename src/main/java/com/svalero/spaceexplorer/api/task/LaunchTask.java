package com.svalero.spaceexplorer.api.task;

import com.svalero.spaceexplorer.api.model.Launch;
import com.svalero.spaceexplorer.api.service.SpaceDevsService;
import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.Objects;

public class LaunchTask extends Task<Integer> {

    private LocalDate reqDate;
    Integer records;
    private Consumer<Launch> launchConsumer;

    public LaunchTask(Consumer<Launch> launchConsumer, Integer records, LocalDate reqDate) {
        this.launchConsumer = launchConsumer;
        this.records = records;
        this.reqDate = Objects.requireNonNullElseGet(reqDate, LocalDate::now);
    }


    @Override
    protected Integer call() {
        SpaceDevsService spaceDevsService = new SpaceDevsService();
        spaceDevsService.getLaunchInfo(records, reqDate).subscribe(launchConsumer);
        return null;
    }
}
