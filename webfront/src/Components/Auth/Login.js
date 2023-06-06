import styles from "./Login.module.css"
import { Alert, Backdrop, Button, CircularProgress, Dialog, DialogTitle, Link, TextField } from "@mui/material";
import { DoLogin } from "../../Services/LoginService";
import { useUserContext } from "../../UserContextProvider";
import { useRef, useState } from "react";
import LoginIcon from '@mui/icons-material/Login';
import { useApi } from "../../Services/GenericServiceHook";
import { getUserInfoParams } from "../../Services/UserApi";
import { FormattedMessage } from "react-intl";
import { useNavigate } from "react-router-dom";

const Login = () => {
    const navigate = useNavigate();
    const userInfoApi = useApi();
    const [ loginFailed, setLoginFailed ] = useState( false );
    const { logout, setUserInfo, isLoggedIn } = useUserContext();
    const [ processing, setProcessing ] = useState( false );
    const formRef = useRef();

    const lostPassword = () => {
        formRef.current.reset();
        setLoginFailed( false );
        navigate( '/lost-password' );
    }

    const formSubmitted = ( event ) => {
        event.preventDefault();

        setProcessing( true );
        logout();
        const formData = new FormData( event.target );
        DoLogin( Object.fromEntries( formData.entries() ) ).then( result => {
            if ( result ) {
                userInfoApi.request( { ...getUserInfoParams(), token: result } ).then( async userInfoResult => {
                    const userInfo = await userInfoResult.json();
                    setLoginFailed( false );
                    setUserInfo( { ...userInfo, token: result } );
                } ).catch( () => {
                    setLoginFailed( true );
                } )
            } else {
                setLoginFailed( true );
            }
        } ).finally( () => {
            setProcessing( false );
        } );
    }

    return (
        <Dialog className={ styles.dialog } open={ !isLoggedIn() }>
            <DialogTitle><FormattedMessage id="login.dialog-title"/></DialogTitle>
            <form className={ styles.form } onSubmit={ formSubmitted } ref={ formRef }>
                { loginFailed &&
                    <Alert severity={ "error" }><FormattedMessage id="login.wrong-login-or-password"/></Alert> }
                <TextField name={ "username" } label={ <FormattedMessage id="login.login"/> } variant={ "standard" }/>
                <TextField name={ "password" } label={ <FormattedMessage id="login.password"/> } type={ "password" }
                           variant={ "standard" }
                           autoComplete={ "current-password" }/>
                { loginFailed && <Link onClick={ lostPassword } href={ "#" } color={ "inherit" }
                                       rel="noreferrer"><FormattedMessage
                    id="login.reset-password"/></Link> }
                <Button className={ styles.submitButton } variant={ "contained" } type={ "submit" }
                        startIcon={ <LoginIcon/> }><FormattedMessage id="login.login-button"/></Button>
            </form>
            <Backdrop open={ processing }>
                <CircularProgress/>
            </Backdrop>
        </Dialog>
    );
}

export default Login;
