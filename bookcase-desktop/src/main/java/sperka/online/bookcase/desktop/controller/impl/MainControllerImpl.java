package sperka.online.bookcase.desktop.controller.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import sperka.online.bookcase.desktop.App;
import sperka.online.bookcase.desktop.client.BookClient;
import sperka.online.bookcase.desktop.controller.AddDialogController;
import sperka.online.bookcase.desktop.controller.MainController;
import sperka.online.bookcase.desktop.controller.SettingsDialogController;
import sperka.online.bookcase.desktop.controller.SynchronizationController;
import sperka.online.bookcase.desktop.entity.Book;
import sperka.online.bookcase.desktop.helpers.Jfx;
import sperka.online.bookcase.desktop.interfaces.OnShownEvent;
import sperka.online.bookcase.desktop.service.BookService;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

@Controller
public class MainControllerImpl implements MainController, Initializable, OnShownEvent {
    final BookClient client;
    final BookService bookService;
    final SynchronizationController synchronizationController;
    private final ObservableList< Book > observableBooks = FXCollections.observableArrayList();
    private final FilteredList< Book > filteredBooks = new FilteredList<>( observableBooks );
    private final SortedList< Book > sortableBooks = new SortedList<>( filteredBooks );
    Logger logger = LoggerFactory.getLogger( MainControllerImpl.class );
    @FXML
    TableView< Book > tvBooks;
    @FXML
    TextField tfAuthor;
    @FXML
    TextField tfTitle;
    @FXML
    TextField tfPublished;
    @FXML
    TextField tfSignature;
    @FXML
    Label lVersion;
    @FXML
    Label lConnection;
    @FXML
    Button bSettings;
    @FXML
    HBox hbStatusBar;
    @FXML
    Button bRemove;
    @FXML
    Button bAdd;
    private ResourceBundle bundle;

    @Value( "${build.version}" )
    private String buildVersion;

    public MainControllerImpl( BookClient client, BookService bookService, SynchronizationController synchronizationController ) {
        this.client = client;
        this.bookService = bookService;
        this.synchronizationController = synchronizationController;
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {
        bundle = resources;

        lVersion.setText( MessageFormat.format( bundle.getString( "main.statusbar.version" ), buildVersion ) );

        populateColumns();
        filteredBooks.setPredicate( getBooksPredicate() );

        synchronizationController.synchronize();

        observableBooks.addAll( bookService.getAll() );
        tvBooks.setItems( sortableBooks );
        sortableBooks.comparatorProperty().bind( tvBooks.comparatorProperty() );
        tvBooks.refresh();

        tvBooks.setRowFactory( tv -> {
            var row = new TableRow< Book >();
            row.setOnMouseClicked( mouseEvent -> {
                if ( mouseEvent.getClickCount() == 2 && (!row.isEmpty()) ) {
                    editBook( row.getItem() );
                }
            } );
            return row;
        } );

        bAdd.setOnAction( actionEvent -> showAddBookDialog() );
        bRemove.setOnAction( actionEvent -> deleteSelectedBook() );
        bSettings.setOnAction( actionEvent -> showSettingsDialog() );
    }

    private void editBook( Book book ) {
        try {
            var loader = Jfx.getFxmlLoader( "addDialog", bundle, App.getContext() );
            var stage = new Stage();
            var scene = new Scene( loader.load(), -1, -1, true, SceneAntialiasing.BALANCED );
            stage.setScene( scene );
            stage.initOwner( tvBooks.getScene().getWindow() );
            stage.initModality( Modality.APPLICATION_MODAL );
            AddDialogController controller = loader.getController();

            controller.setTitle( book.getTitle() );
            controller.setAuthor( book.getAuthor() );
            controller.setPublished( book.getPlaceAndYear() );
            controller.setSignature( book.getSignature() );

            stage.showAndWait();

            if ( controller.getResult() ) {
                UpdateBook( book, controller );
            }
        } catch ( IOException e ) {
            logger.error( e.getMessage(), e );
        }
    }

    private void populateColumns() {
        TableColumn< Book, String > authorColumn = new TableColumn<>( bundle.getString( "main.table.header.author" ) );
        authorColumn.setCellValueFactory( new PropertyValueFactory<>( "author" ) );
        TableColumn< Book, String > titleColumn = new TableColumn<>( bundle.getString( "main.table.header.title" ) );
        titleColumn.setCellValueFactory( new PropertyValueFactory<>( "title" ) );
        TableColumn< Book, String > publishedColumn = new TableColumn<>( bundle.getString( "main.table.header.published" ) );
        publishedColumn.setCellValueFactory( new PropertyValueFactory<>( "placeAndYear" ) );
        TableColumn< Book, String > signatureColumn = new TableColumn<>( bundle.getString( "main.table.header.signature" ) );
        signatureColumn.setCellValueFactory( new PropertyValueFactory<>( "signature" ) );

        tvBooks.getColumns().clear();
        tvBooks.getColumns().add( authorColumn );
        tvBooks.getColumns().add( titleColumn );
        tvBooks.getColumns().add( publishedColumn );
        tvBooks.getColumns().add( signatureColumn );
    }

    private Predicate< Book > getBooksPredicate() {
        return book -> {
            String author = tfAuthor.getText().toLowerCase();
            String title = tfTitle.getText().toLowerCase();
            String published = tfPublished.getText().toLowerCase();
            String signature = tfSignature.getText().toLowerCase();

            boolean authorCondition = author.isBlank() || book.getAuthor().toLowerCase().contains( author );
            boolean titleCondition = title.isBlank() || book.getTitle().toLowerCase().contains( title );
            boolean publishedCondition = published.isBlank() || book.getPlaceAndYear().toLowerCase().contains( published );
            boolean signatureCondition = signature.isBlank() || book.getSignature().toLowerCase().contains( signature );

            return authorCondition && titleCondition && publishedCondition && signatureCondition;
        };
    }

    @Override
    public void refreshFilter() {
        filteredBooks.setPredicate( getBooksPredicate() );
    }

    @Override
    public void onShown( WindowEvent event ) {
        tvBooks.getScene().addEventFilter( KeyEvent.KEY_PRESSED, keyEvent -> {
            final KeyCombination addBookKeyComb = new KeyCodeCombination( KeyCode.N, KeyCombination.SHORTCUT_DOWN );
            final KeyCombination deleteBookKeyComb = new KeyCodeCombination( KeyCode.DELETE );
            if ( addBookKeyComb.match( keyEvent ) ) {
                showAddBookDialog();
            } else if ( deleteBookKeyComb.match( keyEvent ) ) {
                deleteSelectedBook();
            }
        } );
    }

    // TODO: SYNC FROM DESKTOP TO SERVER ON ACTION OR TIMER
    // TODO: Get (on timer) last modify date from server == heartbeat + if sync needed
    private void deleteSelectedBook() {
        var deleted = new AtomicBoolean( false );
        tvBooks.getSelectionModel().getSelectedItems().forEach( b -> {
            var alert = new Alert( Alert.AlertType.CONFIRMATION, MessageFormat.format( bundle.getString( "main.delete.question" ), b.getTitle() ), ButtonType.YES, ButtonType.NO );
            var result = alert.showAndWait();
            if ( result.isPresent() && result.get() == ButtonType.YES ) {
                observableBooks.remove( b );
                b.setDeleted( true );
                b.setModifyDate( LocalDateTime.now().toInstant( ZoneOffset.UTC ) );
                bookService.save( b );
                deleted.set( true );
            }
        } );
        if ( deleted.get() ) {
            synchronizationController.synchronize();
        }
    }

    private void showSettingsDialog() {
        try {
            var loader = Jfx.getFxmlLoader( "settingsDialog", bundle, App.getContext() );
            var stage = new Stage();
            var scene = new Scene( loader.load(), -1, -1, true, SceneAntialiasing.BALANCED );
            stage.setScene( scene );
            stage.initOwner( tvBooks.getScene().getWindow() );
            stage.initModality( Modality.APPLICATION_MODAL );
            stage.showAndWait();
            SettingsDialogController controller = loader.getController();
            if ( controller.getResult() ) {
                // TODO:
            }
        } catch ( IOException e ) {
            logger.error( e.getMessage(), e );
        }
    }

    private void showAddBookDialog() {
        try {
            var loader = Jfx.getFxmlLoader( "addDialog", bundle, App.getContext() );
            var stage = new Stage();
            var scene = new Scene( loader.load(), -1, -1, true, SceneAntialiasing.BALANCED );
            stage.setScene( scene );
            stage.initOwner( tvBooks.getScene().getWindow() );
            stage.initModality( Modality.APPLICATION_MODAL );
            stage.showAndWait();
            AddDialogController controller = loader.getController();
            if ( controller.getResult() ) {
                AddBook( controller );
            }
        } catch ( IOException e ) {
            logger.error( e.getMessage(), e );
        }
    }

    private void AddBook( AddDialogController controller ) {
        logger.info( "Adding book: " + controller.getAuthor() + " " + controller.getTitle() + " " + controller.getPublished() + " " + controller.getSignature() );

        var book = new Book( null, null, controller.getAuthor(), controller.getTitle(), controller.getPublished(),
                controller.getSignature(), LocalDateTime.now().toInstant( ZoneOffset.UTC ),
                LocalDateTime.now().toInstant( ZoneOffset.UTC ), false );

        if ( !bookService.exists( book ) ) {
            bookService.save( book );
            observableBooks.add( book );
            synchronizationController.synchronize();
            tvBooks.refresh();
        }
    }

    private void UpdateBook( Book book, AddDialogController controller ) {
        logger.info( "Updating book( " + book.toString() + " ): " + controller.getAuthor() + " " + controller.getTitle() + " " + controller.getPublished() + " " + controller.getSignature() );

        if ( book.getSignature().equals( controller.getSignature() ) ) {
            book.setAuthor( controller.getAuthor() );
            book.setTitle( controller.getTitle() );
            book.setPlaceAndYear( controller.getPublished() );
            book.setSignature( controller.getSignature() );
            book.setModifyDate( LocalDateTime.now().toInstant( ZoneOffset.UTC ) );

            bookService.save( book );
        } else {
            logger.info( "Signature has changed - deleting id = " + book.getId() );
            // Signature has changed - delete book
            observableBooks.remove( book );
            book.setDeleted( true );
            book.setModifyDate( LocalDateTime.now().toInstant( ZoneOffset.UTC ) );
            bookService.save( book );

            var newBook = new Book();
            newBook.setAuthor( controller.getAuthor() );
            newBook.setTitle( controller.getTitle() );
            newBook.setPlaceAndYear( controller.getPublished() );
            newBook.setSignature( controller.getSignature() );
            newBook.setCreateDate( book.getCreateDate() );
            newBook.setModifyDate( LocalDateTime.now().toInstant( ZoneOffset.UTC ) );
            newBook.setDeleted( false );
            bookService.save( newBook );
            logger.info( "Signature has changed - added new book id = " + newBook.getId() );
            observableBooks.add( newBook );
        }

        synchronizationController.synchronize();
        tvBooks.refresh();
    }
}
