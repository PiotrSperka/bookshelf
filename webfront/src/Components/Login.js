import "./Login.css"
import {Alert, Button, Dialog, DialogTitle, TextField} from "@mui/material";
import DoLogin from "../Services/LoginService";
import {useUserContext} from "../UserContextProvider";
import {useState} from "react";

const Login = () => {
    const [loginFailed, setLoginFailed] = useState(false);
    const {logout, setUserToken, isLoggedIn} = useUserContext();

    const formSubmitted = (event) => {
        event.preventDefault();

        logout();
        const formData = new FormData(event.target);
        DoLogin(Object.fromEntries(formData.entries())).then(result => {
            if (result) {
                setLoginFailed(false);
                setUserToken(result);
            } else {
                setLoginFailed(true);
            }
        });
    }

    return (
        <Dialog open={!isLoggedIn()} >
            <DialogTitle>You need to log in</DialogTitle>
            <form className="form" onSubmit={formSubmitted}>
                {loginFailed && <Alert severity={"error"}>Wrong login or password!</Alert>}
                <TextField name={"username"} label={"Login"} variant={"standard"}/>
                <TextField name={"password"} label={"Password"} type={"password"} variant={"standard"}
                           autoComplete={"current-password"}/>
                <Button className="submitButton" variant={"contained"} type={"submit"}>Login</Button>
            </form>
        </Dialog>
    );
}

export default Login;
