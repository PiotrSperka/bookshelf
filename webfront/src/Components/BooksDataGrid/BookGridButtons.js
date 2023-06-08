import { Box, Button } from "@mui/material";
import styles from "./BookGridButtons.module.css";
import AddIcon from "@mui/icons-material/Add";
import { FormattedMessage } from "react-intl";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import RefreshIcon from "@mui/icons-material/Refresh";
import AddEditBook from "./AddEditBook";
import DeleteBook from "./DeleteBook";
import { useEffect, useState } from "react";

const BookGridButtons = props => {
    const [ addDialogOpen, setAddDialogOpen ] = useState( false );
    const [ editDialogOpen, setEditDialogOpen ] = useState( false );
    const [ deleteDialogOpen, setDeleteDialogOpen ] = useState( false );

    useEffect( () => {
        if ( props.editTrigger !== null && props.selectedBookId != null ) {
            setEditDialogOpen( true );
        }
    }, [ props.editTrigger ] )

    const handleBooksChanged = refresh => {
        setAddDialogOpen( false );
        setEditDialogOpen( false );
        setDeleteDialogOpen( false );
        if ( refresh === true && props.onBooksChanged !== null ) {
            props.onBooksChanged();
        }
    }

    return (
        <Box>
            <AddEditBook open={ addDialogOpen } bookId={ null } onClose={ handleBooksChanged }/>
            <AddEditBook open={ editDialogOpen } bookId={ props.selectedBookId } onClose={ handleBooksChanged }/>
            <DeleteBook open={ deleteDialogOpen } bookId={ props.selectedBookId } onClose={ handleBooksChanged }/>
            <Button className={ styles.button } variant={ "contained" } startIcon={ <AddIcon/> } onClick={ () => {
                setAddDialogOpen( true );
            } }><FormattedMessage id="book-grid.add"/></Button>
            <Button className={ styles.button } variant={ "outlined" } startIcon={ <EditIcon/> }
                    disabled={ props.selectedBookId === null } onClick={ () => {
                setEditDialogOpen( true );
            } }><FormattedMessage id="book-grid.edit"/></Button>
            <Button className={ styles.button } variant={ "outlined" } startIcon={ <DeleteIcon/> } color={ "error" }
                    disabled={ props.selectedBookId === null }
                    onClick={ () => setDeleteDialogOpen( true ) }><FormattedMessage
                id="book-grid.delete"/></Button>
            <Button className={ styles.button } variant={ "outlined" } startIcon={ <RefreshIcon/> }
                    onClick={ () => handleBooksChanged( true ) }><FormattedMessage
                id="book-grid.refresh"/></Button>
        </Box>
    )
}

export default BookGridButtons;
