package com.internhub.exception;
public class InternshipClosedException extends RuntimeException { public InternshipClosedException() { super("This internship is not open for applications."); } }
