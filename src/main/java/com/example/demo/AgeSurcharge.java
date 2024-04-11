package com.example.demo;

import lombok.Getter;

import java.util.Calendar;
import java.util.Date;

public class AgeSurcharge {
    private Integer range1;
    private Integer range2;
    private Integer range3;
    private Integer range4;

    public AgeSurcharge(Integer range1, Integer range2, Integer range3, Integer range4) {
        this.range1 = range1;
        this.range2 = range2;
        this.range3 = range3;
        this.range4 = range4;
    }
    public Integer getAgeSurcharge(Date age) throws Exception {
        // Obtiene el año actual
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);

        // Obtiene el año del objeto Date
        Calendar ageCalendar = Calendar.getInstance();
        ageCalendar.setTime(age);
        int ageYear = ageCalendar.get(Calendar.YEAR);
        // Calcula la antiguedad
        int yearsOld = currentYear - ageYear;
        System.out.println(yearsOld);

        if (0 <= yearsOld && yearsOld <= 5)
            return range1;
        if (6 <= yearsOld && yearsOld <= 10)
            return range2;
        if (11 <= yearsOld && yearsOld <= 15)
            return range3;
        if (16 <= yearsOld)
            return range4;
        throw new Exception("Age of surcharge out of range");
    }

}

