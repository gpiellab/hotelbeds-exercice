package com.hacker.exercice.service;

import com.hacker.exercice.model.Logs;

public interface Counter {

    boolean hasTooManyFailedAttempts(Logs logEntry);

    void init();

    int getAttemptCacheSize();
}
