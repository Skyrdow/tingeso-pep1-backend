package com.example.demo;

import lombok.Getter;

public class DiscountByRepair {
    private Integer range1;
    private Integer range2;
    private Integer range3;
    private Integer range4;

    public DiscountByRepair(Integer range1, Integer range2, Integer range3, Integer range4) {
        this.range1 = range1;
        this.range2 = range2;
        this.range3 = range3;
        this.range4 = range4;
    }

    public Integer getDiscountByRepair(Integer repairCount) throws Exception {
        if (repairCount == 0) return 0;
        if (1 <= repairCount && repairCount <= 2)
            return range1;
        if (3 <= repairCount && repairCount <= 5)
            return range2;
        if (6 <= repairCount && repairCount <= 9)
            return range3;
        if (10 <= repairCount)
            return range4;
        throw new Exception("Repair count discount out of range");
    }
}
