import "./DeleteBook.css"
import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@mui/material";
import { useApi } from "../Services/GenericServiceHook";
import { getDeleteSingleBookParams } from "../Services/BooksApi";
import { useEffect } from "react";
import { FormattedMessage } from "react-intl";

const DeleteBook = ( props ) => {
    const deleteApi = useApi();

    const handleClose = () => {
        if ( props.onClose ) {
            props.onClose( false );
        }
    }

    const handleDelete = () => {
        deleteApi.request( getDeleteSingleBookParams( props.bookId ) );
    }

    useEffect( () => {
        if ( deleteApi.data !== null && deleteApi.error === "" && deleteApi.loading === false && props.onClose ) {
            props.onClose( true );
        }
    }, [ deleteApi.loading ] )

    return (
        <Dialog open={ props.open }>
            <DialogTitle><FormattedMessage id="delete-book.dialog-title"/></DialogTitle>
            <DialogContent>
                <DialogContentText>
                    <FormattedMessage id="delete-book.question"/>
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={ handleDelete }><FormattedMessage id="delete-book.yes"/></Button>
                <Button onClick={ handleClose } autoFocus><FormattedMessage id="delete-book.no"/></Button>
            </DialogActions>
        </Dialog>
    )
}

export default DeleteBook;
