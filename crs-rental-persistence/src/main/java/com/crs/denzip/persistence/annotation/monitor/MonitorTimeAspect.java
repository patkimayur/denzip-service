package com.crs.denzip.persistence.annotation.monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;

@Aspect
public class MonitorTimeAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(MonitorTimeAspect.class);

  @Around("@annotation(MonitorTime)")
  public Object monitorTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    long startTime = System.nanoTime();
    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
    MonitorTime monitorTimeAnnotation = AnnotationUtils.findAnnotation(methodSignature.getMethod(), MonitorTime.class);
    Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(monitorTimeAnnotation);
    String category = (String) annotationAttributes.get("category");
    String subCategory = (String) annotationAttributes.get("subCategory");
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      long endTime = System.nanoTime();
      LOGGER.info("[{}] [{}] [{}] [{}] : {} ms", category, subCategory, startTime / 1000000, endTime / 1000000, (endTime - startTime) / 1000000);
    }
  }
}
