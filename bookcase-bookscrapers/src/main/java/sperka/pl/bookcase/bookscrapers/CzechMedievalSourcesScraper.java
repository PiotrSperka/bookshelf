package sperka.pl.bookcase.bookscrapers;

import org.apache.hc.core5.net.URIBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sperka.pl.bookcase.bookscrapers.tools.DefaultJsoupDocumentLoader;
import sperka.pl.bookcase.bookscrapers.tools.IDocumentLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class CzechMedievalSourcesScraper {
    private final IDocumentLoader documentLoader;

    public CzechMedievalSourcesScraper() {
        documentLoader = new DefaultJsoupDocumentLoader();
    }

    public CzechMedievalSourcesScraper( IDocumentLoader documentLoader ) {
        this.documentLoader = documentLoader;
    }

    private int getMaxPage( String url ) throws IOException {
        try {
            var doc = documentLoader.load( url );
            var builder = new URIBuilder( doc.selectFirst( "i.fa-step-forward" ).parent().absUrl( "href" ) );
            var queryParams = builder.getQueryParams();
            var pageParam = queryParams.stream().filter( q -> q.getName().equals( "page" ) ).findFirst().orElseThrow();

            return Integer.parseInt( pageParam.getValue() );
        } catch ( URISyntaxException e ) {
            System.err.println( e.getMessage() );
        } catch ( NullPointerException e ) {
            System.err.println( e.getMessage() );
        }

        return -1;
    }

    @Nullable
    private Image getPageImage( String bookUrl, int page ) {
        try {
            var builder = new URIBuilder( bookUrl );
            builder.setParameter( "page", Integer.toString( page ) );
            var doc = documentLoader.load( builder.toString() );
            var img = doc.select( "img#page" ).first();
            if ( img != null ) {
                return ImageIO.read( new URL( img.absUrl( "src" ) ) );
            }
        } catch ( Exception e ) {
            System.err.println( "Cannot fetch the page" );
        }

        return null;
    }

    public @NotNull String getBookTitle( String url ) throws IOException {
        var title = "";

        try {
            var doc = documentLoader.load( url );
            var sideMenu = doc.selectFirst( "div#page_listing" ).html();
            title = sideMenu.split( "<br>" )[ 0 ].trim();
        } catch ( NullPointerException e ) {
            System.err.println( e.getMessage() );
        }

        return title;
    }

    public @NotNull ByteArrayOutputStream getBookAsPdf( String url ) throws IOException {
        var output = new ByteArrayOutputStream();

        try ( var pdf = new PDDocument() ) {
            for ( int i = 1; i <= getMaxPage( url ); i++ ) {
                var image = getPageImage( url, i );
                if ( image != null ) {
                    var page = new PDPage( new PDRectangle( image.getWidth( null ), image.getHeight( null ) ) );
                    pdf.addPage( page );
                    var pdImageXObject = LosslessFactory.createFromImage( pdf, ( BufferedImage ) image );
                    try ( var contentStream = new PDPageContentStream( pdf, page ) ) {
                        contentStream.drawImage( pdImageXObject, 0, 0 );
                    }
                }
            }

            pdf.save( output );
        }

        return output;
    }
}
