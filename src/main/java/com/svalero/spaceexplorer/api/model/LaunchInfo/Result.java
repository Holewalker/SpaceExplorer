package com.svalero.spaceexplorer.api.model.LaunchInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    String name;
    Status status;
    String window_start; //TODO date
    LaunchServiceProvider launchServiceProvider;
    Rocket rocket;
    Mission mission;
    Pad pad;


}
