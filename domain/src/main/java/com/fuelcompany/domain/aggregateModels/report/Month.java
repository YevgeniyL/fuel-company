package com.fuelcompany.domain.aggregateModels.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Month {
    private Integer year;
    private List<RecordByMonthItem> records;
}
