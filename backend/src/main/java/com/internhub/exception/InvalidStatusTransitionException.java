package com.internhub.exception;
public class InvalidStatusTransitionException extends RuntimeException { public InvalidStatusTransitionException() { super("That application status transition is not allowed."); } }
