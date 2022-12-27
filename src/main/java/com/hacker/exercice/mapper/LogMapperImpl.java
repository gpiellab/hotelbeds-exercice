package com.hacker.exercice.mapper;

import org.springframework.stereotype.Component;

import com.hacker.exercice.model.Logs;
import com.hacker.exercice.model.Logs.LoginAction;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.StringTokenizer;

@Component
public class LogMapperImpl implements LogMapper {

	private StringTokenizer validateLine(String line) {
        if (line == null) {
            throw new LogMapperException("Input line is null!");
        }

        StringTokenizer stringTokenizer = new StringTokenizer(line, ",");

        if (stringTokenizer.countTokens() != 4) {
            throw new LogMapperException("Erroneous line format: " + line);
        }

        return stringTokenizer;
    }

    private LoginAction parseLoginAction(String actionString) {

        if (LoginAction.SIGNIN_FAILURE.name().equals(actionString)) {
            return LoginAction.SIGNIN_FAILURE;
        } else if (LoginAction.SIGNIN_SUCCESS.name().equals(actionString)) {
            return LoginAction.SIGNIN_SUCCESS;
        } else {
            throw new LogMapperException("Invalid login action: " + actionString);
        }
    }

	
    public Logs parse(String line) {

        StringTokenizer stringTokenizer = validateLine(line);

        String ipAddress = stringTokenizer.nextToken();
        String epochInSeconds = stringTokenizer.nextToken();
        String actionString = stringTokenizer.nextToken();
        String userName = stringTokenizer.nextToken();

        
        return new Logs(ipAddress, parseEpoch(epochInSeconds), parseLoginAction(actionString), userName);
    
    }

    
    private LocalDateTime parseEpoch(String epochInSeconds) {

        try {
            long epochInMillis = Long.valueOf(epochInSeconds) * 1000;
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochInMillis), ZoneId.systemDefault());
        } catch (NumberFormatException e) {
            throw new LogMapperException("Invalid epoch format: " + epochInSeconds, e);
        }
    }

}
