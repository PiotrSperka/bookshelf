import styles from "./DeleteUser.module.css"
import { useApi } from "../Services/GenericServiceHook";
import { useEffect } from "react";
import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@mui/material";
import { deleteUserParams } from "../Services/UserApi";

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
            <DialogTitle>Confirm</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Do you want to delete selected user?
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleDelete}>Yes</Button>
                <Button onClick={handleClose} autoFocus>No</Button>
            </DialogActions>
        </Dialog>
    )
}

export default DeleteUser;

