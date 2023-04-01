package sperka.online.bookcase.desktop.controller.impl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import sperka.online.bookcase.desktop.controller.AddDialogController;

@Controller
public class AddDialogControllerImpl implements AddDialogController {
    private final Logger logger = LoggerFactory.getLogger( AddDialogControllerImpl.class );

    @FXML
    TextField tfAuthor;
    @FXML
    TextField tfTitle;
    @FXML
    TextField tfPublished;
    @FXML
    TextField tfSignature;
    boolean result;

    public AddDialogControllerImpl() {
        setResult( false );
    }

    @Override
    public void onSave( ActionEvent event ) {
        var stage = ( Stage ) tfAuthor.getScene().getWindow();
        setResult( true );
        stage.close();
    }

    @Override
    public void onCancel( ActionEvent event ) {
        var stage = ( Stage ) tfAuthor.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setTitle( String title ) {
        tfTitle.setText( title );
    }

    @Override
    public void setAuthor( String author ) {
        tfAuthor.setText( author );
    }

    @Override
    public void setPublished( String published ) {
        tfPublished.setText( published );
    }

    @Override
    public void setSignature( String signature ) {
        tfSignature.setText( signature );
    }

    @Override
    public String getTitle() {
        return tfTitle.getText();
    }

    @Override
    public String getAuthor() {
        return tfAuthor.getText();
    }

    @Override
    public String getPublished() {
        return tfPublished.getText();
    }

    @Override
    public String getSignature() {
        return tfSignature.getText();
    }

    @Override
    public boolean getResult() {
        return result;
    }

    private void setResult( boolean r ) {
        result = r;
    }
}
