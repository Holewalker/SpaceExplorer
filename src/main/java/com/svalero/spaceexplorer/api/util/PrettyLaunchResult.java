package com.svalero.spaceexplorer.api.util;

import com.svalero.spaceexplorer.api.model.LaunchInfo.Result;

public class PrettyLaunchResult {


    public static String prettyfyResult(Result result) {
        String s = "";
        String nl = "\n";
        s = "name: " + result.getName() + nl
                + "Launch provider: " + result.getLaunch_service_provider().getName() + nl
                + "Launch date: " + result.getWindow_start() + nl
                + "Rocket: " + result.getRocket().getConfiguration().getName() + nl
                + "Mission: " + result.getMission().getName() + nl
                + "Pad: " + result.getPad().getName() + nl
                + "Status: " + result.getStatus().getDescription() + nl;

        return s;
    }
}
