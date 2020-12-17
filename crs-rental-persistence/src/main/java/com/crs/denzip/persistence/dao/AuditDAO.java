package com.crs.denzip.persistence.dao;

import com.crs.denzip.model.marshaller.CRSMarshaller;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.Types;

@AllArgsConstructor
public class AuditDAO extends AbstractDAO  {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuditDAO.class);

  private final CRSMarshaller crsMarshaller;

  private static final String ADD_AUDIT = "insert into crs_audit (input, result, audit_type) "
      + "values (cast(:input as json), :result, :audit_type);";

  public int auditInfo(Object input, Object result, String auditType) {
    MapSqlParameterSource paramSourceMap = new MapSqlParameterSource();
    paramSourceMap.addValue("input", new SqlParameterValue(Types.VARCHAR, crsMarshaller.marshall(input)));
    paramSourceMap.addValue("result", new SqlParameterValue(Types.VARCHAR, crsMarshaller.marshall(result)));
    paramSourceMap.addValue("audit_type", new SqlParameterValue(Types.VARCHAR, auditType));

    return super.update(ADD_AUDIT, paramSourceMap);
  }

}
