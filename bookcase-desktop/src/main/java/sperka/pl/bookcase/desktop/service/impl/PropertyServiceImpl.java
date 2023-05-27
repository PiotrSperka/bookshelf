package sperka.pl.bookcase.desktop.service.impl;

import org.springframework.stereotype.Service;
import sperka.pl.bookcase.desktop.entity.Property;
import sperka.pl.bookcase.desktop.repository.PropertyRepository;
import sperka.pl.bookcase.desktop.service.PropertyService;

@Service
public class PropertyServiceImpl implements PropertyService {
    final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public boolean propertyExists(String key) {
        Property property = propertyRepository.findByKey(key);
        return property != null;
    }

    @Override
    public String getPropertyKey(String key) {
        Property property = propertyRepository.findByKey(key);
        return (property != null) ? property.getValue() : "";
    }

    @Override
    public Property getProperty(String key) {
        return propertyRepository.findByKey(key);
    }

    @Override
    public void setProperty(String key, String value) {
        Property property = propertyRepository.findByKey(key);
        if (property == null) {
            property = new Property();
            property.setKey(key);
        }

        property.setValue(value);
        propertyRepository.save(property);
    }
}
