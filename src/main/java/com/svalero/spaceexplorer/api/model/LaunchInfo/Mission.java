package com.svalero.spaceexplorer.api.model.LaunchInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mission {

    Integer id;
    String name;
    String description;
    String type;
    Orbit orbit;

}
