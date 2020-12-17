package com.crs.denzip.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaySchedule {
  private String name;
  private boolean selected;
  private HourSchedule startTime;
  private HourSchedule endTime;
  private List<VisitSlot> visitSlots;
}
