package com.hacker.exercice;

import com.hacker.exercice.mapper.LogMapper;
import com.hacker.exercice.mapper.LogMapperImpl;
import com.hacker.exercice.model.Logs;
import com.hacker.exercice.service.Counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HackerDetectorImpl implements HackerDetector {

    private LogMapper logEntryParser;

    private Counter attemptCounter;

    @Autowired
    public HackerDetectorImpl(LogMapperImpl logEntryParser, Counter attemptCounter) {
        this.logEntryParser = logEntryParser;
        this.attemptCounter = attemptCounter;
    }

    public String parseLine(String line) {

        Logs logEntry = logEntryParser.parse(line);

        if (attemptCounter.hasTooManyFailedAttempts(logEntry)) {
            return logEntry.getIpAddress();
        } else {
            return null;
        }
    }

    void init() {
        attemptCounter.init();
    }

    int getAttemptCounterChacheSize() {
        return attemptCounter.getAttemptCacheSize();
    }

}
