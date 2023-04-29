import "./TopBar.css"
import {AppBar, Box, Toolbar, Typography} from "@mui/material";
import {useUserContext} from "../UserContextProvider";
import TopBarUserMenu from "./TopBarUserMenu";

const TopBar = () => {
    const {isLoggedIn} = useUserContext();

    return (
        <Box>
            <AppBar position={"static"}>
                <Toolbar>
                    <Typography variant={"h6"} noWrap component={"div"} sx={{flexGrow: 1}}>
                        Bookshelf: The book collection database
                    </Typography>
                    {isLoggedIn() && <TopBarUserMenu />}
                </Toolbar>
            </AppBar>
        </Box>
    )
}

export default TopBar;
