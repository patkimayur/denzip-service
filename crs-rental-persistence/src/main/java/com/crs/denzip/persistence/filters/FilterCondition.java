package com.crs.denzip.persistence.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FilterCondition {

  private String condition;
  private boolean applied;

  @Override
  public String toString() {
    return this.condition;
  }
}


//propertyType : FilterCondition[] = [
//    {condition: "Apartment", applied: true},
//    {condition :"Villa", applied: false},
//    {condition: "Row House", applied: true}
//    ];
//
//    export class FilterCondition {
//  condition: string;
//  applied: boolean;
//}
//
//<div class="btn-group btn-group-toggle">
//
//<label class="btn-primary" ngbButtonLabel *ngFor="let type of propertyType">
//<input type="checkbox" ngbButton [(ngModel)]="type.applied"> {{type.condition}}
//</label>
//</div>