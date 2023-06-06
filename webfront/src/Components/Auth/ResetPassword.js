import styles from "./ResetPassword.module.css"
import { Alert, Backdrop, Button, CircularProgress, Dialog, DialogTitle, TextField } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import { FormattedMessage, useIntl } from "react-intl";
import LoginIcon from "@mui/icons-material/Login";
import { useApi } from "../../Services/GenericServiceHook";
import { useEffect, useState } from "react";
import { getResetPasswordParams } from "../../Services/UserApi";

const ResetPassword = () => {
    const navigate = useNavigate();
    const intl = useIntl();
    const params = useParams();
    const resetApi = useApi();
    const [ validationErrors, setValidationErrors ] = useState( {} );

    useEffect( () => {
        if ( resetApi.data !== null ) {
            if ( resetApi.data.status !== 200 ) {
                resetApi.data.clone().json().then( json => {
                    setValidationErrors( json );
                } ).catch( () => {
                    setValidationErrors( {} );
                } );
            } else {
                setValidationErrors( {} );
                navigate( '/' );
            }
        }
    }, [ resetApi.data ] )

    const formSubmitted = ( event ) => {
        event.preventDefault();

        const formData = new FormData( event.target );
        resetApi.request( getResetPasswordParams( params[ '*' ], Object.fromEntries( formData.entries() ) ) )
    }

    return (
        <Dialog className={ styles.dialog } open={ true }>
            <DialogTitle><FormattedMessage id="reset-password.dialog-title"/></DialogTitle>
            <form className={ styles.form } onSubmit={ formSubmitted }>
                { ( 'generic' in validationErrors ) &&
                    <Alert severity={ "error" }><FormattedMessage id={ validationErrors[ 'generic' ] }/></Alert> }
                <TextField name={ "password" } label={ <FormattedMessage id="reset-password.password"/> }
                           type={ "password" }
                           variant={ "standard" }/>
                <TextField name={ "passwordRepeat" } label={ <FormattedMessage id="reset-password.password-repeat"/> }
                           type={ "password" } error={ 'password' in validationErrors }
                           helperText={ 'password' in validationErrors && intl.formatMessage( { id: validationErrors[ 'password' ] } ) }
                           variant={ "standard" }/>
                <Button className={ styles.submitButton } variant={ "contained" } type={ "submit" }
                        startIcon={ <LoginIcon/> }><FormattedMessage id="reset-password.save-button"/></Button>
            </form>
            <Backdrop open={ resetApi.loading }>
                <CircularProgress/>
            </Backdrop>
        </Dialog>
    )
}

export default ResetPassword;
