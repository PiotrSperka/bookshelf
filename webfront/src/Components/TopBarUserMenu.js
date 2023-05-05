import "./TopBarUserMenu.css"
import { Avatar, Box, IconButton, Menu, MenuItem, Tooltip, Typography } from "@mui/material";
import { useUserContext } from "../UserContextProvider";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const TopBarUserMenu = () => {
    const navigate = useNavigate();
    const { logout, hasRole } = useUserContext();
    const [ anchorElUser, setAnchorElUser ] = useState( null );

    const handleOpenUserMenu = ( event ) => {
        setAnchorElUser( event.currentTarget );
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser( null );
    };

    const handleUserSettings = () => {
        navigate( '/user' );
        handleCloseUserMenu();
    }

    const handleSystemSettings = () => {
        navigate( '/admin' );
        handleCloseUserMenu();
    }

    const handleDatabaseSettings = () => {
        navigate( '/' );
        handleCloseUserMenu();
    }

    const handleLogout = () => {
        logout();
        handleCloseUserMenu();
    }

    return (
        <Box sx={ { flexGrow: 0 } }>
            <Tooltip title={ "Open settings" }>
                <IconButton onClick={ handleOpenUserMenu } sx={ { p: 0 } }>
                    <Avatar alt={ "TODO: Username..." }/>
                </IconButton>
            </Tooltip>
            <Menu anchorEl={ anchorElUser } anchorOrigin={ { vertical: 'bottom', horizontal: 'right' } } keepMounted
                  transformOrigin={ { vertical: 'top', horizontal: 'right' } } open={ Boolean( anchorElUser ) }
                  onClose={ handleCloseUserMenu }>
                <MenuItem onClick={ handleDatabaseSettings }>
                    <Typography className={ "menuLabel" } textAlign={ "center" }>Database</Typography>
                </MenuItem>
                <MenuItem onClick={ handleUserSettings }>
                    <Typography className={ "menuLabel" } textAlign={ "center" }>Settings</Typography>
                </MenuItem>
                { hasRole( "admin" ) && <MenuItem onClick={ handleSystemSettings }>
                    <Typography className={ "menuLabel" } textAlign={ "center" }>Administration</Typography>
                </MenuItem> }
                <MenuItem onClick={ handleLogout }>
                    <Typography className={ "menuLabel" } textAlign={ "center" }>Logout</Typography>
                </MenuItem>
            </Menu>
        </Box>
    )
}

export default TopBarUserMenu;
