package com.svalero.spaceexplorer.api.model.LaunchInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    Integer id;
    String name;
    String country_code;
    }
