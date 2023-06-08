import styles from "./Installer.module.css"
import { Alert, Backdrop, Button, CircularProgress, Dialog, DialogTitle, TextField } from "@mui/material";
import { useEffect, useRef, useState } from "react";
import LoginIcon from '@mui/icons-material/Login';
import { FormattedMessage, useIntl } from "react-intl";
import { useApi } from "../../Services/GenericServiceHook";
import { getInitializeUserParams } from "../../Services/InstallerApi";

const Installer = props => {
    const intl = useIntl();
    const saveApi = useApi()
    const [ validationErrors, setValidationErrors ] = useState( {} );
    const formRef = useRef();

    useEffect( () => {
        if ( saveApi.data !== null ) {
            if ( saveApi.data.status !== 200 ) {
                saveApi.data.clone().json().then( json => {
                    setValidationErrors( json );
                } ).catch( () => {
                    setValidationErrors( {} );
                } );
            } else {
                setValidationErrors( {} );
                if ( props.onCreated ) {
                    props.onCreated();
                }
            }
        }
    }, [ saveApi.data ] )

    const formSubmitted = ( event ) => {
        event.preventDefault();

        const formData = new FormData( event.target );
        saveApi.request( getInitializeUserParams( Object.fromEntries( formData.entries() ) ) )
    }

    return (
        <Dialog className={ styles.dialog } open={ true }>
            <DialogTitle><FormattedMessage id="installer.dialog-title"/></DialogTitle>
            <form className={ styles.form } onSubmit={ formSubmitted } ref={ formRef }>
                <Alert severity={ "info" }><FormattedMessage id="installer.info"/></Alert>
                { saveApi.error && Object.keys( validationErrors ).length === 0 &&
                    <Alert severity={ "error" }>{ saveApi.error }</Alert> }
                <TextField name={ "name" } label={ <FormattedMessage id="installer.name"/> } variant={ "standard" }
                           error={ 'name' in validationErrors }
                           helperText={ 'name' in validationErrors && intl.formatMessage( { id: validationErrors[ 'name' ] } ) }/>
                <TextField name={ "email" } label={ <FormattedMessage id="installer.email"/> } type={ "email" }
                           variant={ "standard" } error={ 'email' in validationErrors }
                           helperText={ 'email' in validationErrors && intl.formatMessage( { id: validationErrors[ 'email' ] } ) }/>
                <TextField name={ "password" } label={ <FormattedMessage id="installer.password"/> } type={ "password" }
                           error={ 'password' in validationErrors }
                           helperText={ 'password' in validationErrors && intl.formatMessage( { id: validationErrors[ 'password' ] } ) }
                           variant={ "standard" }/>
                <Button className={ styles.submitButton } variant={ "contained" } type={ "submit" }
                        startIcon={ <LoginIcon/> }><FormattedMessage id="installer.create"/></Button>
            </form>
            <Backdrop open={ saveApi.loading }>
                <CircularProgress/>
            </Backdrop>
        </Dialog>
    );
}

export default Installer;
