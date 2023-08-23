package sperka.pl.bookcase.bookscrapers;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sperka.pl.bookcase.bookscrapers.tools.IDocumentLoader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class CzechMedievalSourcesScraperTest {
    private final String realHtml = """
            <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"  "http://www.w3.org/TR/html4/strict.dtd" >
            <HTML lang="cs">
            <HEAD>
                    <META http-equiv="Content-Type" content="text/html; charset=utf-8">
                    <TITLE>Filozofický ústav AV</TITLE>
            \t        <META NAME="description" CONTENT="">
            \t\t        <META NAME="keywords" CONTENT="">
            \t        <LINK href="style/default2.css" rel="stylesheet" type="text/css">
                       <script src="https://kit.fontawesome.com/087a55c7ea.js" crossorigin="anonymous"></script>

            \t</HEAD>
            \t\t\t\t\t\t
            <body><!-- Global site tag (gtag.js) - Google Analytics -->
            <script async src="https://www.googletagmanager.com/gtag/js?id=UA-49354205-5"></script>
            <script>
              window.dataLayer = window.dataLayer || [];
              function gtag(){dataLayer.push(arguments);}
              gtag('js', new Date());

              gtag('config', 'UA-49354205-5');
            </script>

            <!-- Global site tag (gtag.js) - Google Analytics -->
            <script async src="https://www.googletagmanager.com/gtag/js?id=UA-7870090-1"></script>
            <script>
              window.dataLayer = window.dataLayer || [];
              function gtag(){dataLayer.push(arguments);}
              gtag('js', new Date());

              gtag('config', 'UA-7870090-1');
            </script>

            <div id='container'><div id="logo">
                <h1> Czech medieval sources  FONTES</h1>
            </div>


            <div id="menu">
            <div id='biblodkaz'> </div> <div id="menublock"><table class="pagecontrol">
            <tr><td style='width:40px;'><a href='?s=v&amp;cat=6'> <i class='fas fa-undo'> </i></a></td><td style="width:90px;"><a href='?s=v&amp;bookid=88&amp;fulltext=' >full text</a></td><td style="width:200px;white-space:nowrap;" >
            \t\t\t<form action="?" method="get" style="display:inline">
            \t\t\t<input type="hidden" name="s" value="v" >
            \t\t\t<input type="hidden" name="action" value="jdi" >
            \t\t\t<input type="hidden" name="cat" value="6" >
            \t\t\t<input type="hidden" name="bookid" value="88" >
            \t\t\t<input type="text" size="3" name="page" value="" >\s
            \t\t\t<input type="submit" alt="jdi na"  name="action_button" class="goto" value="\uD83D\uDCD6\uFE01">
            \t\t\t</form>
            \t\t</td><td style="width:40px;"><i class="fas fa-step-backward"></i> </td><td style='width:40px;'><i class="fas fa-angle-double-left"></i></td><td style='width:40px;'><i class='fas fa-angle-left'></i></td><td id='page_number' style='width:200px;'></td><td style='width:40px;'><a href='?s=v&amp;cat=6&amp;bookid=88&amp;page=2'><i class='fas fa-angle-right'></i></a></td><td style='width:40px;'><a href='?s=v&amp;cat=6&amp;bookid=88&amp;page=11'> <i class='fas fa-angle-double-right'></i> </a></td><td style='width:40px;'><a href='?s=v&amp;cat=6&amp;bookid=88&amp;page=296'> <i class='fas fa-step-forward'></i> </a></td><td style='width:40px;'><a href='print.php?id=88&amp;page=1' target='_blank' > <i class='fas fa-print'></i></a></td> </tr></table></div><a id="adminlink" href="?s=a">administrace</a>
            </div>

            <div id='page_listing'>
            CDS 15 Acta Nicolai Gramis<br ><a href='?s=v&amp;cat=6&amp;bookid=88&amp;page=0'>Titel</a> <br ><a href='?s=v&amp;cat=6&amp;bookid=88&amp;page=5'>Vorwort</a> <br ><a href='?s=v&amp;cat=6&amp;bookid=88&amp;page=17'>Acta Nicolai Gramis</a> <br ><a href='?s=v&amp;cat=6&amp;bookid=88&amp;page=281'>Register</a> <br ></div>
            <div id='page_view_zoom'><div class="img-overlay-wrap"><img id="page" src="online/88/1.jpg"  alt=" "></div>
            </div>
            <small>2356906</small></div></body></html>""";

    @Mock
    IDocumentLoader documentLoader;

    @Test
    void whenPageIsCorrectScraperShouldReturnTitle() throws IOException {
        var doc = Jsoup.parse( realHtml );
        when( documentLoader.load( any( String.class ) ) ).thenReturn( doc );
        var scraper = new CzechMedievalSourcesScraper( documentLoader );

        var title = scraper.getBookTitle( "" );

        assertEquals( "CDS 15 Acta Nicolai Gramis", title );
    }

    @Test
    void whenPageIsEmptyScraperShouldReturnEmptyTitle() throws IOException {
        var emptyDoc = Jsoup.parse( "<html></html>" );
        when( documentLoader.load( any( String.class ) ) ).thenReturn( emptyDoc );
        var scraper = new CzechMedievalSourcesScraper( documentLoader );

        var title = scraper.getBookTitle( "" );

        assertEquals( "", title );
    }

    @Test
    void whenPageIsNotAvailableScraperShouldThrowWhenGettingTitle() throws IOException {
        when( documentLoader.load( any( String.class ) ) ).thenThrow( new IOException() );
        var scraper = new CzechMedievalSourcesScraper( documentLoader );

        assertThrows( IOException.class, () -> scraper.getBookTitle( "" ) );
    }

    @Test
    void whenPageIsNotAvailableScraperShouldThrowWhenGettingPdf() throws IOException {
        when( documentLoader.load( any( String.class ) ) ).thenThrow( new IOException() );
        var scraper = new CzechMedievalSourcesScraper( documentLoader );

        assertThrows( IOException.class, () -> scraper.getBookAsPdf( "" ) );
    }

    @Test
    void whenPageIsEmptyScraperShouldReturnEmptyPdf() throws IOException {
        var emptyDoc = Jsoup.parse( "<html></html>" );
        when( documentLoader.load( any( String.class ) ) ).thenReturn( emptyDoc );
        var scraper = new CzechMedievalSourcesScraper( documentLoader );

        var pdf = scraper.getBookAsPdf( "" );

        assertNotNull( pdf );
    }
}