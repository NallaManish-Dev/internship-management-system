package com.internhub.entity;
import java.time.Instant;
public class ApplicationStatusHistory { private ApplicationStatus status; private Instant changedAt; public ApplicationStatus getStatus(){return status;} public void setStatus(ApplicationStatus v){status=v;} public Instant getChangedAt(){return changedAt;} public void setChangedAt(Instant v){changedAt=v;} }
