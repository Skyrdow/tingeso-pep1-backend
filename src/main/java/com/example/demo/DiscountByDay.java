package com.example.demo;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DiscountByDay {
    private Integer discount;
    private Integer firstWeekDay = 1;
    private Integer lastWeekDay = 4;
    private Integer firstHour = 9;
    private Integer lastHour = 12;

    public DiscountByDay(Integer discount) {
        this.discount = discount;
    }

    public Integer getDiscountByDay(Date retrievalDate) {
        // Convertir Date a LocalDateTime
        LocalDateTime localRetrievalDate = retrievalDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Obtener el día de la semana y la hora
        int weekDay = localRetrievalDate.getDayOfWeek().getValue(); // Lunes = 1, Domingo = 7
        int hour = localRetrievalDate.getHour();

        // Verificar si es lunes (1) o jueves (4) y si la hora está entre las 09:00 y las 12:00
        if ((weekDay == firstWeekDay || weekDay == lastWeekDay) && (hour >= firstHour && hour < lastHour)) {
            return discount;
        } else {
            return 0;
        }
    }
}
