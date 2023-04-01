package sperka.online.bookcase.desktop.controller.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import sperka.online.bookcase.desktop.controller.SettingsDialogController;

@Controller
public class SettingsDialogControllerImpl implements SettingsDialogController {
    @FXML
    CheckBox cbUseServer;
    @FXML
    TextField tfServerAddress;
    @FXML
    Button bSave;
    @FXML
    Button bCancel;

    boolean result;

    public SettingsDialogControllerImpl() {

    }

    @Override
    public void onSave( ActionEvent event ) {
        result = true;
        var stage = ( Stage )bCancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public void onCancel( ActionEvent event ) {
        result = false;
        var stage = ( Stage )bCancel.getScene().getWindow();
        stage.close();
    }

    @Override
    public boolean getResult() {
        return result;
    }
}
