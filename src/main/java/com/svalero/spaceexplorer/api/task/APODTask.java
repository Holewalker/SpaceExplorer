package com.svalero.spaceexplorer.api.task;

import com.svalero.spaceexplorer.api.model.APOD;
import com.svalero.spaceexplorer.api.service.NasaService;
import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.util.Objects;

public class APODTask extends Task<Integer> {

    private LocalDate reqDate;
    private Consumer<APOD> apodConsumer;
    private String apiKey;

    public APODTask(Consumer<APOD> apodConsumer, String apiKey, LocalDate reqDate) {
        this.apodConsumer = apodConsumer;
        this.apiKey = apiKey;
        this.reqDate = Objects.requireNonNullElseGet(reqDate, LocalDate::now);
    }


    @Override
    protected Integer call() {
        NasaService nasaService = new NasaService();
        nasaService.getAPOD(reqDate, apiKey)
                .subscribe(apodConsumer);
        return null;
    }
}
