import "./TopBarUserMenu.css"
import {Avatar, Box, IconButton, Menu, MenuItem, Tooltip, Typography} from "@mui/material";
import {useUserContext} from "../UserContextProvider";
import {useState} from "react";

const TopBarUserMenu = () => {
    const {logout} = useUserContext();
    const [anchorElUser, setAnchorElUser] = useState(null);

    const handleOpenUserMenu = (event) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    const handleLogout = () => {
        logout();
        handleCloseUserMenu();
    }

    return (
        <Box sx={{flexGrow: 0}}>
            <Tooltip title={"Open settings"}>
                <IconButton onClick={handleOpenUserMenu} sx={{p: 0}}>
                    <Avatar alt={"TODO: Username..."}/>
                </IconButton>
            </Tooltip>
            <Menu anchorEl={anchorElUser} anchorOrigin={{vertical: 'bottom', horizontal: 'right'}} keepMounted
                  transformOrigin={{vertical: 'top', horizontal: 'right'}} open={Boolean(anchorElUser)}
                  onClose={handleCloseUserMenu}>
                <MenuItem onClick={handleCloseUserMenu}>
                    <Typography className={"menuLabel"} textAlign={"center"}>Settings</Typography>
                </MenuItem>
                <MenuItem onClick={handleLogout}>
                    <Typography className={"menuLabel"} textAlign={"center"}>Logout</Typography>
                </MenuItem>
            </Menu>
        </Box>
    )
}

export default TopBarUserMenu;
