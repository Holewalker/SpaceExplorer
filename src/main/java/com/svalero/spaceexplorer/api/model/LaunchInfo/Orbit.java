package com.svalero.spaceexplorer.api.model.LaunchInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orbit {
    Integer id;
    String name;
    String abbrev;
}
