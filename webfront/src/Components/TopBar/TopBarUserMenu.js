import styles from "./TopBarUserMenu.module.css"
import { Avatar, Box, IconButton, Menu, MenuItem, Tooltip, Typography } from "@mui/material";
import { useUserContext } from "../../UserContextProvider";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FormattedMessage } from "react-intl";

const TopBarUserMenu = () => {
    const navigate = useNavigate();
    const { logout, hasRole, user } = useUserContext();
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
            <Tooltip title={ <FormattedMessage id="topmenu.open-settings"/> }>
                <IconButton onClick={ handleOpenUserMenu } sx={ { p: 0 } }>
                    <Avatar alt={ user.name }/>
                </IconButton>
            </Tooltip>
            <Menu anchorEl={ anchorElUser } anchorOrigin={ { vertical: 'bottom', horizontal: 'right' } } keepMounted
                  transformOrigin={ { vertical: 'top', horizontal: 'right' } } open={ Boolean( anchorElUser ) }
                  onClose={ handleCloseUserMenu }>
                <MenuItem onClick={ handleDatabaseSettings }>
                    <Typography className={ styles.menuLabel } textAlign={ "center" }><FormattedMessage
                        id="topmenu.database"/></Typography>
                </MenuItem>
                <MenuItem onClick={ handleUserSettings }>
                    <Typography className={ styles.menuLabel } textAlign={ "center" }><FormattedMessage
                        id="topmenu.settings"/></Typography>
                </MenuItem>
                { hasRole( "admin" ) && <MenuItem onClick={ handleSystemSettings }>
                    <Typography className={ styles.menuLabel } textAlign={ "center" }><FormattedMessage
                        id="topmenu.administration"/></Typography>
                </MenuItem> }
                <MenuItem onClick={ handleLogout }>
                    <Typography className={ styles.menuLabel } textAlign={ "center" }><FormattedMessage
                        id="topmenu.logout"/></Typography>
                </MenuItem>
            </Menu>
        </Box>
    )
}

export default TopBarUserMenu;
