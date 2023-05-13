package sperka.online.bookcase.server.service;

import sperka.online.bookcase.server.dto.LogDto;
import sperka.online.bookcase.server.dto.LogFilterDto;

import java.util.List;

public interface LogService {
    void add( String log, String username );

    List< LogDto > getPaginated( int page, int perPage, LogFilterDto filters );

    Long getCount( LogFilterDto filters );
}
