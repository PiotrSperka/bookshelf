import styles from "./TopBarUserMenu.module.css"
import { Avatar, Box, IconButton, ListItemIcon, Menu, MenuItem, Tooltip, Typography } from "@mui/material";
import { useUserContext } from "../../UserContextProvider";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FormattedMessage } from "react-intl";
import StorageIcon from '@mui/icons-material/Storage';
import SettingsIcon from '@mui/icons-material/Settings';
import CloudDownloadIcon from '@mui/icons-material/CloudDownload';
import SupervisorAccountIcon from '@mui/icons-material/SupervisorAccount';
import LogoutIcon from '@mui/icons-material/Logout';

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

    const handleBookScraping = () => {
        navigate( '/book-scraping' );
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
                    <ListItemIcon>
                        <StorageIcon fontSize={ "small" }/>
                    </ListItemIcon>
                    <Typography className={ styles.menuLabel }><FormattedMessage
                        id="topmenu.database"/></Typography>
                </MenuItem>
                <MenuItem onClick={ handleUserSettings }>
                    <ListItemIcon>
                        <SettingsIcon fontSize={ "small" }/>
                    </ListItemIcon>
                    <Typography className={ styles.menuLabel }><FormattedMessage
                        id="topmenu.settings"/></Typography>
                </MenuItem>
                <MenuItem onClick={ handleBookScraping }>
                    <ListItemIcon>
                        <CloudDownloadIcon fontSize={ "small" }/>
                    </ListItemIcon>
                    <Typography className={ styles.menuLabel }><FormattedMessage
                        id="topmenu.book-scraping"/></Typography>
                </MenuItem>
                { hasRole( "admin" ) && <MenuItem onClick={ handleSystemSettings }>
                    <ListItemIcon>
                        <SupervisorAccountIcon fontSize={ "small" }/>
                    </ListItemIcon>
                    <Typography className={ styles.menuLabel }><FormattedMessage
                        id="topmenu.administration"/></Typography>
                </MenuItem> }
                <MenuItem onClick={ handleLogout }>
                    <ListItemIcon>
                        <LogoutIcon fontSize={ "small" }/>
                    </ListItemIcon>
                    <Typography className={ styles.menuLabel }><FormattedMessage
                        id="topmenu.logout"/></Typography>
                </MenuItem>
            </Menu>
        </Box>
    )
}

export default TopBarUserMenu;
