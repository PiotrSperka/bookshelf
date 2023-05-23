package sperka.pl.bookcase.desktop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sperka.pl.bookcase.desktop.entity.Book;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository< Book, Long > {
    List<Book> deletedIsFalse();

    List< Book > findBySignatureAndDeletedIsFalse( String signature );

    Book findByRemoteId( Long remoteId );

    @Query( "select e from #{#entityName} e where e.modifyDate > :modificationDate" )
    List< Book > findModifiedAfter( Instant modificationDate );
}
