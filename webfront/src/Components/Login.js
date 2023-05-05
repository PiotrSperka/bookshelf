import "./Login.css"
import { Alert, Backdrop, Button, CircularProgress, Dialog, DialogTitle, TextField } from "@mui/material";
import DoLogin from "../Services/LoginService";
import { useUserContext } from "../UserContextProvider";
import { useState } from "react";
import LoginIcon from '@mui/icons-material/Login';
import { useApi } from "../Services/GenericServiceHook";
import { getUserInfoParams } from "../Services/UserApi";

const Login = () => {
    const userInfoApi = useApi();
    const [ loginFailed, setLoginFailed ] = useState( false );
    const { logout, setUserInfo, isLoggedIn } = useUserContext();
    const [ processing, setProcessing ] = useState( false );

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
                } ).catch( err => {
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
        <Dialog open={ !isLoggedIn() }>
            <DialogTitle>You need to log in</DialogTitle>
            <form className="form" onSubmit={ formSubmitted }>
                { loginFailed && <Alert severity={ "error" }>Wrong login or password!</Alert> }
                <TextField name={ "username" } label={ "Login" } variant={ "standard" }/>
                <TextField name={ "password" } label={ "Password" } type={ "password" } variant={ "standard" }
                           autoComplete={ "current-password" }/>
                <Button className="submitButton" variant={ "contained" } type={ "submit" }
                        startIcon={ <LoginIcon/> }>Login</Button>
            </form>
            <Backdrop open={ processing }>
                <CircularProgress/>
            </Backdrop>
        </Dialog>
    );
}

export default Login;
