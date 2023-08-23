package sperka.pl.bookcase.bookscrapers.tools;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface IDocumentLoader {
    Document load( String url ) throws IOException;
}
