package com.example.demo;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DelaySurcharge {
    private Integer surchargeValue;

    public DelaySurcharge(Integer surchargeValue) {
        this.surchargeValue = surchargeValue;
    }

    public Integer getDelaySurcharge(Date repairExitDate, Date retrievalDate) throws Exception {
        Long diffInMs = Math.abs(retrievalDate.getTime() - repairExitDate.getTime());
        if (diffInMs < 0)
            throw new Exception("Delay surcharge difference is negative");

        // Convierte la diferencia en días
        Integer diffInDays = Math.toIntExact(TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS));
        return diffInDays * surchargeValue;
    }
}
