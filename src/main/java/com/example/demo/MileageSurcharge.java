package com.example.demo;

import lombok.Getter;

public class MileageSurcharge {
    private Integer range1;
    private Integer range2;
    private Integer range3;
    private Integer range4;
    private Integer range5;

    public MileageSurcharge(Integer range1, Integer range2, Integer range3, Integer range4, Integer range5) {
        this.range1 = range1;
        this.range2 = range2;
        this.range3 = range3;
        this.range4 = range4;
        this.range5 = range5;
    }
    public Integer getMileageSurcharge(Long mileage) throws Exception{
        if (0 <= mileage && mileage <= 5000)
            return range1;
        if (5001 <= mileage && mileage <= 12000)
            return range2;
        if (12001 <= mileage && mileage <= 25000)
            return range3;
        if (25001 <= mileage && mileage <= 40000)
            return range4;
        if (40001 <= mileage)
            return range5;
        throw new Exception("Mileage surcharge out of range");
    }
}
