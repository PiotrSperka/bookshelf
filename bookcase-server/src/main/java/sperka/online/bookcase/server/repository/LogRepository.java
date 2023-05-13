package sperka.online.bookcase.server.repository;

import sperka.online.bookcase.server.dto.LogFilterDto;
import sperka.online.bookcase.server.entity.Log;

import java.util.List;

public interface LogRepository extends BasicRepository< Log > {
    List< Log > getPaginated( int page, int perPage, LogFilterDto filters );

    Long getCount( LogFilterDto filters );
}
