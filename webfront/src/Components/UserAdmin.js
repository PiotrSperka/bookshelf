import styles from "./UserAdmin.module.css"
import { Alert, Box, Button, Card, CardContent, TextField, Typography } from "@mui/material";
import { useApi } from "../Services/GenericServiceHook";
import { changeUserPasswordParams } from "../Services/UserApi";
import { FormattedMessage } from "react-intl";

const UserAdmin = () => {
    const passwordApi = useApi();

    const formSubmitted = event => {
        event.preventDefault();
        passwordApi.reset();
        const formData = new FormData( event.target );
        passwordApi.request( changeUserPasswordParams( Object.fromEntries( formData.entries() ) ) );
    }

    return (
        <Box className={ styles.main }>
            <Card className={ styles.card }>
                <CardContent>
                    { !!passwordApi.error && <Alert severity={ "error" }>Error: { passwordApi.error }</Alert> }
                    { ( !!passwordApi.data && passwordApi.data.status === 200 ) &&
                        <Alert severity={ "success" }><FormattedMessage
                            id="user-settings.password-changed-successfully"/></Alert> }
                    <Typography gutterBottom variant={ "h5" } component={ "div" }>
                        <FormattedMessage id="user-settings.change-password"/>
                    </Typography>
                    <form className={ styles.form } onSubmit={ formSubmitted }>
                        <TextField name={ "currentPassword" }
                                   label={ <FormattedMessage id="user-settings.current-password"/> } type={ "password" }
                                   variant={ "standard" }/>
                        <TextField name={ "newPassword" } label={ <FormattedMessage id="user-settings.new-password"/> }
                                   type={ "password" }
                                   variant={ "standard" }/>
                        <TextField name={ "newPasswordRepeat" }
                                   label={ <FormattedMessage id="user-settings.retype-new-password"/> }
                                   type={ "password" }
                                   variant={ "standard" }/>
                        <Button className={ styles.submitButton } variant={ "contained" }
                                type={ "submit" }><FormattedMessage id="user-settings.change-password-button"/></Button>
                    </form>
                </CardContent>
            </Card>
        </Box>
    )
}

export default UserAdmin;
