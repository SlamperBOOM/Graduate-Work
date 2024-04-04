package com.slamperboom.backend.mathematics.resultData;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public record SeriesValue(@Schema(description = "Date of value", example = "2023-12-31") Date date,
                          Double value){}
