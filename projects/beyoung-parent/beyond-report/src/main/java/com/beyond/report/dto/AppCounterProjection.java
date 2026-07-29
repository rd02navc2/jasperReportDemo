package com.beyond.report.dto;

import java.util.Date;

public interface AppCounterProjection {
    Date getLPJ04();
    Integer getCOUNTER_ALL();
    Integer getCOUNTER_000();
    Integer getCOUNTER_APP();
    Integer getCOUNTER_BEYOND();
    Integer getCOUNTER_NON_BEYOND();
}