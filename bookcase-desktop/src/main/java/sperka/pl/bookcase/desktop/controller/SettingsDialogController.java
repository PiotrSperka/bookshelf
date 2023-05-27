package sperka.pl.bookcase.desktop.controller;

import javafx.event.ActionEvent;

public interface SettingsDialogController {
    void onSave( ActionEvent event );

    void onCancel( ActionEvent event );

    boolean getResult();
}
