package com.epam.hometask.steps;

import com.amazonaws.services.logs.model.FilterLogEventsRequest;
import com.amazonaws.services.logs.model.FilterLogEventsResult;
import com.amazonaws.services.logs.model.FilteredLogEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.epam.hometask.CommonBase.*;

public class ServicesSteps {
    public static boolean isLambdaInvoked(String uploadedFileName) {
        long startTime = System.nanoTime();
        List<String> lambdaInvocationList = new ArrayList<>();
        while(lambdaInvocationList.isEmpty()) {
            FilterLogEventsResult filterLogEventsResult = awsLogs.filterLogEvents(
                    new FilterLogEventsRequest()
                            .withLogGroupName("/aws/lambda/" + lambdaName));
            List<FilteredLogEvent> events = filterLogEventsResult.getEvents();
            lambdaInvocationList = events.stream().map(FilteredLogEvent::toString).filter(i -> i.contains(uploadedFileName))
                    .collect(Collectors.toList());

            // wait for cloudwath log appers, otherwise exit on timeout
            if (TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime) > timeout) {
                return false;
            }
        }

        return true;
    }
}
