package sperka.online.bookcase.server.service.impl;

import sperka.online.bookcase.server.dto.BookDto;
import sperka.online.bookcase.server.entity.Book;
import sperka.online.bookcase.server.repository.BookRepository;
import sperka.online.bookcase.server.service.BookService;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Instant;

@ApplicationScoped
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public BookDto save(@Valid BookDto dto) {
        Book entity = null;
        if (dto.getRemoteId() != null) {
            entity = bookRepository.getById(dto.getRemoteId());
            entity.setAuthor(dto.getAuthor());
            entity.setTitle(dto.getTitle());
            entity.setReleased(dto.getReleased());
            entity.setSignature(dto.getSignature());
        } else {
            entity = new Book();
            entity.fromDto(dto);
            entity.setDeleted(false);
            entity.setCreateDate(Instant.now());
        }

        entity.setModifyDate(Instant.now());
        entity = bookRepository.save(entity);

        return entity.toDto();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookRepository.delete(id);
    }
}
