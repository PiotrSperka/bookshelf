import styles from "./RequestNewPassword.module.css"
import { Alert, Backdrop, Box, Button, CircularProgress, Dialog, DialogTitle, TextField } from "@mui/material";
import { FormattedMessage } from "react-intl";
import LoginIcon from "@mui/icons-material/Login";
import { useApi } from "../../Services/GenericServiceHook";
import { getRequestResetPasswordParams } from "../../Services/UserApi";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const RequestNewPassword = () => {
    const navigate = useNavigate();
    const requestApi = useApi();
    const [ succeeded, setSucceeded ] = useState( false );

    useEffect( () => {
        if ( requestApi.data !== null ) {
            if ( requestApi.data.status === 200 ) {
                setSucceeded( true );
            }
        }
    }, [ requestApi.data ] )

    const formSubmitted = ( event ) => {
        event.preventDefault();

        const formData = new FormData( event.target );
        requestApi.request( getRequestResetPasswordParams( Object.fromEntries( formData.entries() ) ) )
    }

    return (
        <Dialog className={ styles.dialog } open={ true }>
            <DialogTitle><FormattedMessage id="request-reset-password.dialog-title"/></DialogTitle>
            { succeeded &&
                <Box className={ styles.form }>
                    <Alert severity={ "success" }><FormattedMessage
                        id={ "request-reset-password.success" }/></Alert>
                    <Button className={ styles.submitButton } variant={ "contained" } type={ "button" }
                            startIcon={ <LoginIcon/> } onClick={ () => navigate( '/' ) }><FormattedMessage
                        id="request-reset-password.login"/></Button>
                </Box> }
            { !succeeded && <form className={ styles.form } onSubmit={ formSubmitted }>
                { requestApi.error !== '' &&
                    <Alert severity={ "error" }><FormattedMessage id={ requestApi.error }/></Alert> }
                <TextField name={ "email" } label={ <FormattedMessage id="request-reset-password.email"/> }
                           type={ "email" }
                           variant={ "standard" }/>
                <Button className={ styles.submitButton } variant={ "contained" } type={ "submit" }
                        startIcon={ <LoginIcon/> }><FormattedMessage id="request-reset-password.save-button"/></Button>
            </form> }
            <Backdrop open={ requestApi.loading }>
                <CircularProgress/>
            </Backdrop>
        </Dialog>
    )
}

export default RequestNewPassword;
