package com.crs.denzip.model.exception;

@SuppressWarnings("serial")
public class EmailExistsException extends Exception {

  public EmailExistsException(final String message) {
    super(message);
  }

}