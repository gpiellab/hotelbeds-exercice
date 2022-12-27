package com.hacker.exercice.mapper;

import com.hacker.exercice.model.Logs;

public interface LogMapper {

    Logs parse(String line);
}
