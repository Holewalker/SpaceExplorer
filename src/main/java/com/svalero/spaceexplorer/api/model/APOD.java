package com.svalero.spaceexplorer.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APOD {
    Date date;
    String explanation;
    String hdurl;
    String media_type;
    String service_version;
    String title;
    String url;

}
