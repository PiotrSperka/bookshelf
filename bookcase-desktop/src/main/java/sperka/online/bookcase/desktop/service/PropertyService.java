package sperka.online.bookcase.desktop.service;

import sperka.online.bookcase.desktop.entity.Property;

public interface PropertyService {
    boolean propertyExists(String key);
    String getPropertyKey(String key);
    Property getProperty(String key);
    void setProperty(String key, String value);
}
