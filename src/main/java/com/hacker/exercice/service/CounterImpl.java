package com.hacker.exercice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.hacker.exercice.model.Logs;


import static com.hacker.exercice.model.Logs.LoginAction.SIGNIN_SUCCESS;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;




@Component
public class CounterImpl implements Counter {

    private static Map<String, Attempts> failedLoginAttemptsByIp = new ConcurrentHashMap<>();

    @Value("${hackerdetector.timeWindowInMinutes}")
    private int timeWindowInMinutes;

    @Value("${hackerdetector.maxAttempts}")
    private int maxAttempts;

    @Override
    public boolean hasTooManyFailedAttempts(Logs logEntry) {

        if (SIGNIN_SUCCESS.equals(logEntry.getLoginAction())) {
            return false;
        }

        synchronized (this) {
            removeOldAttempts(logEntry);

            Attempts failedLoginAttempts = getFailedLoginAttemptsForIp(logEntry);

            int failedAttemptsCount = failedLoginAttempts.getFailedAttemptsCountWithinTimeWindow(logEntry.getDateTime(), timeWindowInMinutes);
            return failedAttemptsCount >= maxAttempts;
        }
    }

    @Override
    public void init() {
        this.failedLoginAttemptsByIp = new ConcurrentHashMap<>();
    }

    @Override
    public int getAttemptCacheSize() {
        return this.failedLoginAttemptsByIp.size();
    }

    private void removeOldAttempts(Logs logEntry) {

        LocalDateTime lastAttempt = logEntry.getDateTime();

        failedLoginAttemptsByIp.entrySet().stream().forEach(attemptByIp -> {
            if (attemptByIp.getValue().firstAttemptIsOlderThan(lastAttempt, timeWindowInMinutes)) {
                failedLoginAttemptsByIp.remove(attemptByIp.getKey());
            }
        });

    }

    private Attempts getFailedLoginAttemptsForIp(Logs logEntry) {

        Attempts failedLoginAttempts = failedLoginAttemptsByIp.get(logEntry.getIpAddress());
        if (failedLoginAttempts == null) {
            failedLoginAttempts = new Attempts(logEntry.getDateTime());
            failedLoginAttemptsByIp.put(logEntry.getIpAddress(), failedLoginAttempts);
        }

        return failedLoginAttempts;
    }

}
