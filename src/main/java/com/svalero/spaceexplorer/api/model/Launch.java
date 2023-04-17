package com.svalero.spaceexplorer.api.model;

import com.svalero.spaceexplorer.api.model.LaunchInfo.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Launch {
    //TODO
    Integer count;
    String next;
    String previous;
    List<Result> results;


}
