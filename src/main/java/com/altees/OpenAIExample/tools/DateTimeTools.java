package com.altees.OpenAIExample.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class DateTimeTools {

    @Tool(name = "getCurrentDateTime", description = "Get the current date and time based on users location")
    String getCurrentDateTime() {
        log.info("getCurrentDateTime tool called");
        return LocalDateTime.now().toString();
    }

    @Tool(name = "getCurrentTimBasedOnTimeZone", description = "Get the current time based on the provided time zone")
    public String getCurrentTimBasedOnTimeZone(@ToolParam(description = "value represents the time zone") String timeZone) {
        log.info("getCurrentTimBasedOnTimeZone tool called with timeZone: {}", timeZone);
        ZoneId zoneId = ZoneId.of(timeZone);
        return LocalDateTime.now(zoneId).toString();
    }


    @Tool(description = "Set a user alarm for the given time")
    void setAlarm(@ToolParam(description = "Time in ISO-8601 format") String time) {
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("Alarm set for " + alarmTime);
    }
}