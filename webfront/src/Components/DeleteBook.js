import "./DeleteBook.css"
import {Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@mui/material";
import {useApi} from "../Services/GenericServiceHook";
import {getDeleteSingleBookParams} from "../Services/BooksApi";
import {useEffect} from "react";

const DeleteBook = (props) => {
    const deleteApi = useApi();

    const handleClose = () => {
        if (props.onClose) {
            props.onClose(false);
        }
    }

    const handleDelete = () => {
        deleteApi.request(getDeleteSingleBookParams(props.bookId));
    }

    useEffect(() => {
        if (deleteApi.error === "" && deleteApi.loading === false && props.onClose) {
            props.onClose(true);
        }
    }, [deleteApi.error, deleteApi.loading])

    return (
        <Dialog open={props.open}>
            <DialogTitle>Confirm</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Do you want to delete selected book?
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleDelete}>Yes</Button>
                <Button onClick={handleClose} autoFocus>No</Button>
            </DialogActions>
        </Dialog>
    )
}

export default DeleteBook;
