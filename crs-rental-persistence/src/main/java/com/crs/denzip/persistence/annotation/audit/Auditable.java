package com.crs.denzip.persistence.annotation.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Auditable {
AuditingActionType actionType();
/*
be very careful while using resultStorage as RESULT as the whole object will be stored in the audit db
and the db space could get exhausted very quickly
 */
ResultStorageType resultStorage();
}