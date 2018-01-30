package com.af.services;

public interface IJobService {
    boolean sendEmail(String data);

    boolean loadData(String data);

    boolean indexFiles(String data);
}
