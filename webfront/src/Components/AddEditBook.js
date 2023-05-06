import "./AddEditBook.css"
import {Alert, Backdrop, Button, CircularProgress, Dialog, DialogTitle, TextField} from "@mui/material";
import {useEffect, useState} from "react";
import {useApi} from "../Services/GenericServiceHook";
import {getGetSingleBookParams, getSaveBookParams} from "../Services/BooksApi";
import { FormattedMessage } from "react-intl";

const AddEditBook = props => {
    const saveApi = useApi();
    const getBookApi = useApi();
    const [formData, setFormData] = useState({remoteId: null, author: "", title: "", released: "", signature: ""});

    const resetForm = () => {
        setFormData({remoteId: null, author: "", title: "", released: "", signature: ""});
    }

    useEffect(() => {
        if (props.bookId === parseInt(props.bookId, 10) ) {
            getBookApi.request(getGetSingleBookParams(props.bookId));
        } else {
            resetForm();
        }
    }, [props.bookId])

    useEffect(() => {
        if (getBookApi.data !== null) {
            if (getBookApi.data.status === 200) {
                getBookApi.data.clone().json().then(json => {
                    setFormData(json);
                });
            } else {
                resetForm();
            }
        }
    }, [getBookApi.data])

    useEffect(() => {
        if (saveApi.error === "" && saveApi.loading === false && props.onClose) {
            props.onClose(true);
        }
    }, [saveApi.error, saveApi.loading])

    const submitBook = event => {
        event.preventDefault();
        saveApi.request(getSaveBookParams(formData));
    }

    const cancelDialog = () => {
        if (props.onClose) {
            saveApi.reset();
            if (formData.remoteId === null) {
                resetForm();
            }
            props.onClose(false);
        }
    }

    const onAuthorChange = event => {
        setFormData(prevState => ({...prevState, author: event.target.value}));
    }

    const onTitleChange = event => {
        setFormData(prevState => ({...prevState, title: event.target.value}));
    }

    const onReleaseChange = event => {
        setFormData(prevState => ({...prevState, released: event.target.value}));
    }

    const onSignatureChange = event => {
        setFormData(prevState => ({...prevState, signature: event.target.value}));
    }

    return (
        <Dialog open={props.open}>
            <DialogTitle><FormattedMessage id="add-book-dialog.dialog-title" /></DialogTitle>
            <form className={"form"} onSubmit={submitBook}>
                {saveApi.error && <Alert severity={"error"}>{"Error: " + saveApi.error}</Alert>}
                <TextField name={"author"} value={formData.author} label={<FormattedMessage id="add-book-dialog.author" />} variant={"standard"}
                           onChange={onAuthorChange}/>
                <TextField name={"title"} value={formData.title} label={<FormattedMessage id="add-book-dialog.title" />} variant={"standard"}
                           onChange={onTitleChange}/>
                <TextField name={"release"} value={formData.released} label={<FormattedMessage id="add-book-dialog.release" />} variant={"standard"}
                           onChange={onReleaseChange}/>
                <TextField name={"signature"} value={formData.signature} label={<FormattedMessage id="add-book-dialog.signature" />} variant={"standard"}
                           onChange={onSignatureChange}/>
                <Button className="submitButton" variant={"contained"} type={"submit"}><FormattedMessage id="add-book-dialog.save" /></Button>
                <Button className="closeButton" variant={"outlined"} type={"button"}
                        onClick={cancelDialog}><FormattedMessage id="add-book-dialog.cancel" /></Button>
            </form>
            <Backdrop open={saveApi.loading}>
                <CircularProgress />
            </Backdrop>
        </Dialog>
    )
}

export default AddEditBook;
