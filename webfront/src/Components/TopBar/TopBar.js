import { AppBar, Box, Toolbar, Typography } from "@mui/material";
import { useUserContext } from "../../UserContextProvider";
import TopBarUserMenu from "./TopBarUserMenu";
import { FormattedMessage } from "react-intl";
import style from "./TopBar.module.css"
import logo from "../../Images/bar_logo.png"

const TopBar = () => {
    const { isLoggedIn } = useUserContext();

    return (
        <Box>
            <AppBar position={ "static" }>
                <Toolbar>
                    <img className={ style.logo } src={ logo } alt={ "Logo" }/>
                    <Typography variant={ "h6" } noWrap component={ "div" } sx={ { flexGrow: 1 } }>
                        <FormattedMessage id="app.header.name"/>
                    </Typography>
                    { isLoggedIn() && <TopBarUserMenu/> }
                </Toolbar>
            </AppBar>
        </Box>
    )
}

export default TopBar;
