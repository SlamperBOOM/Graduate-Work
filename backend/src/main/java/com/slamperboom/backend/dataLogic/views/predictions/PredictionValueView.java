package com.slamperboom.backend.dataLogic.views.predictions;

import java.util.Date;

public record PredictionValueView(String methodName, Date date, Double value) {
}
