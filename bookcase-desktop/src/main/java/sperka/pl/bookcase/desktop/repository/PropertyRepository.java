package sperka.pl.bookcase.desktop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sperka.pl.bookcase.desktop.entity.Property;

@Repository
public interface PropertyRepository extends JpaRepository< Property, Long> {
    Property findByKey(String key);
}
