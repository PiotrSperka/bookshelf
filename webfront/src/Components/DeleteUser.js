import styles from "./DeleteUser.module.css"
import { useApi } from "../Services/GenericServiceHook";
import { useEffect } from "react";
import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@mui/material";
import { deleteUserParams } from "../Services/UserApi";
import { FormattedMessage } from "react-intl";

const DeleteUser = props => {
    const deleteApi = useApi();

    const handleClose = () => {
        if (props.onClose) {
            props.onClose(false);
        }
    }

    const handleDelete = () => {
        deleteApi.request(deleteUserParams(props.userId));
    }

    useEffect(() => {
        if (deleteApi.error === "" && deleteApi.loading === false && props.onClose) {
            props.onClose(true);
        }
    }, [deleteApi.error, deleteApi.loading])

    return (
        <Dialog open={props.open}>
            <DialogTitle><FormattedMessage id="delete-user.dialog-title" /></DialogTitle>
            <DialogContent>
                <DialogContentText>
                    <FormattedMessage id="delete-user.question" />
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleDelete}><FormattedMessage id="delete-user.yes" /></Button>
                <Button onClick={handleClose} autoFocus><FormattedMessage id="delete-user.no" /></Button>
            </DialogActions>
        </Dialog>
    )
}

export default DeleteUser;

