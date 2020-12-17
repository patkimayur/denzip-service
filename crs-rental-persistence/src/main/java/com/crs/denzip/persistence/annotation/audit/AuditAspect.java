package com.crs.denzip.persistence.annotation.audit;

import com.crs.denzip.persistence.dao.AuditDAO;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.sql.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Aspect
@AllArgsConstructor
public class AuditAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuditAspect.class);
  public static final String COUNT_DEF = "Count - ";
  private static final String RESULT_AVAILABLE_DEF = "Result Available - ";

  AuditDAO auditDAO;

  @AfterReturning(pointcut = "@annotation(Auditable)", returning = "retVal")
  public void logAuditActivity(JoinPoint joinPoint, Object retVal) throws Throwable {
    List<Object> input = null;
    Object output = null;

    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Auditable auditableAnnotation = AnnotationUtils.findAnnotation(methodSignature.getMethod(), Auditable.class);
    Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(auditableAnnotation);
    AuditingActionType auditingActionType = (AuditingActionType) annotationAttributes.get("actionType");
    ResultStorageType resultStorageType = (ResultStorageType) annotationAttributes.get("resultStorage");

    Object[] parameters = joinPoint.getArgs();
    if (parameters.length > 0) {
      input = Arrays.asList(parameters);
    }

    if (null != retVal) {
      if (ResultStorageType.COUNT.equals(resultStorageType)) {
        if (retVal instanceof Array) {
          output = COUNT_DEF + java.lang.reflect.Array.getLength(retVal);
        } else if (retVal instanceof Collection) {
          output = COUNT_DEF + ((Collection) retVal).size();
        } else {
          output = retVal;
        }
      } else if (ResultStorageType.RESULT_AVAILABLE.equals(resultStorageType)) {
        output = RESULT_AVAILABLE_DEF + true;
      } else if (ResultStorageType.RESULT.equals(resultStorageType)) {
        output = retVal;
      }
    }


    try {
      if (!CollectionUtils.isEmpty(input) && null != auditingActionType) {
        auditDAO.auditInfo(input, output, auditingActionType.name());
      }
    } finally {
      LOGGER.info("Audit info for crs audit type - {}, audit input - {}, result - {}", auditingActionType, input, output);
    }
  }

}
