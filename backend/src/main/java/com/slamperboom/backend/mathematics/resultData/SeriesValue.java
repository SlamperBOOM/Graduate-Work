package com.slamperboom.backend.mathematics.resultData;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record SeriesValue(@Schema(description = "Date of value", example = "31-12-2023") String date,
                          Double value){}
