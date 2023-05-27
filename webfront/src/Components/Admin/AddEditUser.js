import styles from "./AddEditUser.module.css"
import {
    Alert,
    Backdrop,
    Button,
    Checkbox,
    CircularProgress,
    Dialog,
    DialogTitle,
    FormControlLabel,
    TextField
} from "@mui/material";
import { useApi } from "../../Services/GenericServiceHook";
import { useEffect, useState } from "react";
import { addUserParams, editUserParams, getUserParams } from "../../Services/UserApi";
import { useUserContext } from "../../UserContextProvider";
import { FormattedMessage } from "react-intl";

const AddEditUser = props => {
    const { user } = useUserContext();
    const saveApi = useApi()
    const getUserApi = useApi()
    const [ formData, setFormData ] = useState( { id: null, name: "", roles: "", password: "", active: true } );
    const [ selfEditing, setSelfEditing ] = useState( false );

    useEffect( () => {
        if ( props.userId === parseInt( props.userId, 10 ) && props.open === true ) {
            getUserApi.request( getUserParams( props.userId ) );
        } else {
            resetForm();
        }
    }, [ props.userId, props.open ] )

    useEffect( () => {
        const val = user.id === props.userId;
        if ( selfEditing !== val ) {
            setSelfEditing( val );
        }
    }, [ user.id, props.userId, selfEditing ] )

    useEffect( () => {
        if ( getUserApi.data !== null ) {
            if ( getUserApi.data.status === 200 ) {
                getUserApi.data.clone().json().then( json => {
                    setFormData( { ...json, password: "", roles: json.roles.join( ',' ) } );
                } );
            } else {
                resetForm();
            }
        }
    }, [ getUserApi.data ] )

    useEffect( () => {
        if ( saveApi.error === "" && saveApi.loading === false && props.onClose ) {
            props.onClose( true );
        }
    }, [ saveApi.error, saveApi.loading ] )

    const resetForm = () => {
        setFormData( { id: null, name: "", roles: "", password: "", active: true } );
    }

    const submitUser = event => {
        event.preventDefault();
        if ( formData.id != null ) {
            saveApi.request( editUserParams( { ...formData, roles: formData.roles.split( ',' ) } ) )
        } else {
            saveApi.request( addUserParams( { ...formData, roles: formData.roles.split( ',' ) } ) )
        }
    }

    const cancelDialog = () => {
        if ( props.onClose ) {
            saveApi.reset();
            if ( formData.id === null ) {
                resetForm();
            }
            props.onClose( false );
        }
    }

    const onNameChange = event => {
        setFormData( prevState => ( { ...prevState, name: event.target.value } ) );
    }

    const onPasswordChange = event => {
        setFormData( prevState => ( { ...prevState, password: event.target.value } ) );
    }

    const onRolesChange = event => {
        setFormData( prevState => ( { ...prevState, roles: event.target.value } ) );
    }

    const onActiveChange = event => {
        setFormData( prevState => ( { ...prevState, active: event.target.checked } ) );
    }

    return (
        <Dialog className={ styles.dialog } open={ props.open }>
            <DialogTitle><FormattedMessage id="user-dialog.dialog-title"/></DialogTitle>
            <form className={ styles.form } onSubmit={ submitUser }>
                { saveApi.error && <Alert severity={ "error" }>{ "Error: " + saveApi.error }</Alert> }
                <TextField name={ "name" } value={ formData.name } label={ <FormattedMessage id="user-dialog.name"/> }
                           variant={ "standard" }
                           onChange={ onNameChange }/>
                <TextField name={ "password" } value={ formData.password }
                           label={ <FormattedMessage id="user-dialog.password"/> } variant={ "standard" }
                           type={ "password" }
                           onChange={ onPasswordChange }/>
                <TextField name={ "roles" } value={ formData.roles }
                           label={ <FormattedMessage id="user-dialog.roles"/> } variant={ "standard" }
                           onChange={ onRolesChange }/>
                <FormControlLabel control={ <Checkbox/> }
                                  label={ <FormattedMessage id="user-dialog.active"/> } name={ "active" }
                                  checked={ formData.active } disabled={ selfEditing } onChange={ onActiveChange }/>
                <Button className={ styles.submitButton } variant={ "contained" } type={ "submit" }><FormattedMessage
                    id="user-dialog.save"/></Button>
                <Button className={ styles.closeButton } variant={ "outlined" } type={ "button" }
                        onClick={ cancelDialog }><FormattedMessage id="user-dialog.cancel"/></Button>
            </form>
            <Backdrop open={ saveApi.loading }>
                <CircularProgress/>
            </Backdrop>
        </Dialog>
    )
}

export default AddEditUser;

