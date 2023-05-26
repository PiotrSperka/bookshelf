import { useApi } from "../../Services/GenericServiceHook";
import { useEffect } from "react";
import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@mui/material";
import { FormattedMessage } from "react-intl";
import { getRestoreBookParams } from "../../Services/BooksApi";

const RestoreBook = props => {
    const restoreApi = useApi();

    const handleClose = () => {
        if ( props.onClose ) {
            props.onClose( false );
        }
    }

    const handleDelete = () => {
        restoreApi.request( getRestoreBookParams( props.bookId ) );
    }

    useEffect( () => {
        if ( restoreApi.error === "" && restoreApi.loading === false && props.onClose ) {
            props.onClose( true );
        }
    }, [ restoreApi.error, restoreApi.loading ] )

    return (
        <Dialog open={ props.open }>
            <DialogTitle><FormattedMessage id="restore-book.dialog-title"/></DialogTitle>
            <DialogContent>
                <DialogContentText>
                    <FormattedMessage id="restore-book.question"/>
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={ handleDelete }><FormattedMessage id="restore-book.yes"/></Button>
                <Button onClick={ handleClose } autoFocus><FormattedMessage id="restore-book.no"/></Button>
            </DialogActions>
        </Dialog>
    )
}

export default RestoreBook;