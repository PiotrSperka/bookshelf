package sperka.online.bookcase.desktop.controller;

import javafx.event.ActionEvent;

public interface AddDialogController {
    void onSave( ActionEvent event );

    void onCancel( ActionEvent event );

    void setTitle( String title );

    void setAuthor( String author );

    void setPublished( String published );

    void setSignature( String signature );

    String getTitle();

    String getAuthor();

    String getPublished();

    String getSignature();

    boolean getResult();
}
