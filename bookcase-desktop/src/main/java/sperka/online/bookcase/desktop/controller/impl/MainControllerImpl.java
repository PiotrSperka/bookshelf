package sperka.online.bookcase.desktop.controller.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import sperka.online.bookcase.desktop.client.BookClient;
import sperka.online.bookcase.desktop.controller.MainController;
import sperka.online.bookcase.desktop.controller.SynchronizationController;
import sperka.online.bookcase.desktop.entity.Book;
import sperka.online.bookcase.desktop.service.BookService;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.function.Predicate;

@Controller
public class MainControllerImpl implements MainController, Initializable {
    final BookClient client;
    final BookService bookService;
    final SynchronizationController synchronizationController;
    private final ObservableList<Book> observableBooks = FXCollections.observableArrayList();
    private final FilteredList<Book> filteredBooks = new FilteredList<>(observableBooks);
    private final SortedList<Book> sortableBooks = new SortedList<>(filteredBooks);
    Logger logger = LoggerFactory.getLogger(MainControllerImpl.class);
    @FXML
    TableView<Book> tvBooks;
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
    private ResourceBundle bundle;

    @Value("${build.version}")
    private String buildVersion;

    public MainControllerImpl(BookClient client, BookService bookService, SynchronizationController synchronizationController) {
        this.client = client;
        this.bookService = bookService;
        this.synchronizationController = synchronizationController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;

        lVersion.setText(MessageFormat.format(bundle.getString("main.statusbar.version"), buildVersion));

        populateColumns();
        filteredBooks.setPredicate(getBooksPredicate());

        synchronizationController.synchronize();

        observableBooks.addAll(bookService.getAll());
        tvBooks.setItems(sortableBooks);
        sortableBooks.comparatorProperty().bind(tvBooks.comparatorProperty());
        tvBooks.refresh();

//        try {
//            var books = client.findAll();
//            observableBooks.addAll(books);
//            tvBooks.setItems(sortableBooks);
//            sortableBooks.comparatorProperty().bind(tvBooks.comparatorProperty());
//            tvBooks.refresh();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
    }

    private void populateColumns() {
        TableColumn<Book, String> authorColumn = new TableColumn<>(bundle.getString("main.table.header.author"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book, String> titleColumn = new TableColumn<>(bundle.getString("main.table.header.title"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, String> publishedColumn = new TableColumn<>(bundle
                .getString("main.table.header.published"));
        publishedColumn.setCellValueFactory(new PropertyValueFactory<>("placeAndYear"));
        TableColumn<Book, String> signatureColumn = new TableColumn<>(bundle
                .getString("main.table.header.signature"));
        signatureColumn.setCellValueFactory(new PropertyValueFactory<>("signature"));

        tvBooks.getColumns().clear();
        tvBooks.getColumns().add(authorColumn);
        tvBooks.getColumns().add(titleColumn);
        tvBooks.getColumns().add(publishedColumn);
        tvBooks.getColumns().add(signatureColumn);
    }

    private Predicate<Book> getBooksPredicate() {
        return book -> {
            String author = tfAuthor.getText().toLowerCase();
            String title = tfTitle.getText().toLowerCase();
            String published = tfPublished.getText().toLowerCase();
            String signature = tfSignature.getText().toLowerCase();

            boolean authorCondition = author.isBlank() || book.getAuthor().toLowerCase().contains(author);
            boolean titleCondition = title.isBlank() || book.getTitle().toLowerCase().contains(title);
            boolean publishedCondition =
                    published.isBlank() || book.getPlaceAndYear().toLowerCase().contains(published);
            boolean signatureCondition =
                    signature.isBlank() || book.getSignature().toLowerCase().contains(signature);

            return authorCondition && titleCondition && publishedCondition && signatureCondition;
        };
    }

    @Override
    public void refreshFilter() {
        filteredBooks.setPredicate(getBooksPredicate());
    }
}
