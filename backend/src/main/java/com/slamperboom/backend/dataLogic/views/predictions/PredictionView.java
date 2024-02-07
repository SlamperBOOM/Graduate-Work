package com.slamperboom.backend.dataLogic.views.predictions;

import java.util.Date;

public record PredictionView(String taxName, String methodName, Date date, Double value) {
}
