package sperka.pl.bookcase.bookscrapers.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DefaultJsoupDocumentLoader implements IDocumentLoader {
    @Override
    public Document load( String url ) throws IOException {
        return Jsoup.connect( url ).get();
    }
}
