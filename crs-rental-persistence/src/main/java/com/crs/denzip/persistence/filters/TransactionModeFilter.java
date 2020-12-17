package com.crs.denzip.persistence.filters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TransactionModeFilter extends AbstractStringConditionFilter<FilterCondition> {


  private static final String TABLE_ALIAS = "listing_transaction_mode";
  private static final String COLUMN_ID = "transaction_mode_name";

  @JsonCreator
  public TransactionModeFilter(@JsonProperty("conditions") List<FilterCondition> conditions) {
    super("Transaction Mode", conditions, TABLE_ALIAS, COLUMN_ID);
  }

  public static String getTableAlias() {
    return TABLE_ALIAS;
  }

  public static String getColumnId() {
    return COLUMN_ID;
  }



  @Override
  public void addParams(MapSqlParameterSource paramSourceMap) {
    List<String> transactionModes = new ArrayList<>();
    transactionModes.addAll(this.appliedConditions);
    transactionModes.add("No Preference");
    paramSourceMap.addValue(this.columnId, new SqlParameterValue(super.columnType, transactionModes));
  }

}
