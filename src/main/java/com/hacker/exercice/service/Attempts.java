package com.hacker.exercice.service;

import java.time.LocalDateTime;

class Attempts {

    private int attempts = 1;

    private LocalDateTime firstAttempt;

    public Attempts(LocalDateTime firstAttempt) {
        this.firstAttempt = firstAttempt;
    }


    public synchronized boolean firstAttemptIsOlderThan(LocalDateTime timeStamp, int timeWindowInMinutes) {
        return firstAttempt.plusMinutes(timeWindowInMinutes).isBefore(timeStamp);
    }

        
    
    
    public synchronized int getFailedAttemptsCountWithinTimeWindow(LocalDateTime lastAttempt, int timeWindowInMinutes) {

        if (firstAttempt.isBefore(lastAttempt) && firstAttempt.plusMinutes(timeWindowInMinutes).isAfter(lastAttempt)) {
            attempts++;
        } else {
            attempts = 1;
            firstAttempt = lastAttempt;
        }

        return attempts;
    }

}
