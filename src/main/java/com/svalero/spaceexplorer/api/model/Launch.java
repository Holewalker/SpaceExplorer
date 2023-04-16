package com.svalero.spaceexplorer.api.model;

import com.svalero.spaceexplorer.api.model.LaunchInfo.Results;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Launch {
    //TODO
    Integer count;
    String next;
    String previous;
    Results results;


}
