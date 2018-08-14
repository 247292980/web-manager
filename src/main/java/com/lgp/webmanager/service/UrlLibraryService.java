package com.lgp.webmanager.service;

public interface UrlLibraryService {
    String getMap(String key);

    void addMaps(String key);

    boolean refreshOne(String key, String newValue);

}
