package sperka.pl.bookcase.server.repository;

import sperka.pl.bookcase.server.dto.LogFilterDto;
import sperka.pl.bookcase.server.entity.Log;

import java.util.List;

public interface LogRepository extends BasicRepository< Log > {
    List< Log > getPaginated( int page, int perPage, LogFilterDto filters );

    Long getCount( LogFilterDto filters );
}
