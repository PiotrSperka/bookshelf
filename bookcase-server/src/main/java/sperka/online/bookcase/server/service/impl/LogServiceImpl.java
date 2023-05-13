package sperka.online.bookcase.server.service.impl;

import sperka.online.bookcase.server.dto.LogDto;
import sperka.online.bookcase.server.dto.LogFilterDto;
import sperka.online.bookcase.server.entity.Log;
import sperka.online.bookcase.server.repository.LogRepository;
import sperka.online.bookcase.server.service.LogService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;

    public LogServiceImpl( LogRepository logRepository ) {
        this.logRepository = logRepository;
    }

    @Override
    @Transactional
    public void add( String log, String username ) {
        var entity = new Log();
        entity.setLog( log );
        entity.setUsername( username );
        logRepository.save( entity );
    }

    @Override
    public List< LogDto > getPaginated( int page, int perPage, LogFilterDto filters ) {
        return logRepository.getPaginated( page, perPage, filters )
                .stream().map( LogDto::fromEntity ).collect( Collectors.toList() );
    }

    @Override
    public Long getCount( LogFilterDto filters ) {
        return logRepository.getCount( filters );
    }
}
