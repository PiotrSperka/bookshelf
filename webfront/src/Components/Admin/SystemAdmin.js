import styles from "./SystemAdmin.module.css"
import { Box, Button, Card, CardContent, Tab, Tabs } from "@mui/material";
import SettingsBackupRestoreIcon from '@mui/icons-material/SettingsBackupRestore';
import { useEffect, useRef, useState } from "react";
import { useUserContext } from "../../UserContextProvider";
import { useNavigate } from "react-router-dom";
import { FormattedMessage } from "react-intl";
import LogsGrid from "./LogsGrid";
import BookGrid from "../BooksDataGrid/BookGrid";
import RestoreBook from "./RestoreBook";
import UsersGrid from "./UsersGrid";

const SystemAdmin = () => {
    const { hasRole, isLoggedIn } = useUserContext();
    const navigate = useNavigate();
    const [ selectedDeletedBookId, setSelectedDeletedBookId ] = useState( null );
    const [ restoreBookDialogVisible, setRestoreBookDialogVisible ] = useState( false );
    const deletedBooksGrid = useRef();
    const [ tabValue, setTabValue ] = useState( 0 );

    useEffect( () => {
        if ( !isLoggedIn || !hasRole( "admin" ) ) {
            navigate( "/" )
        }

    }, [ isLoggedIn ] )

    const onRevertDialogClose = () => {
        setRestoreBookDialogVisible( false );
        if ( deletedBooksGrid.current !== undefined ) {
            deletedBooksGrid.current.refresh();
        }
    }

    return (
        <Box className={ styles.main }>
            <RestoreBook bookId={ selectedDeletedBookId } open={ restoreBookDialogVisible }
                         onClose={ onRevertDialogClose }/>
            <Box sx={ { borderBottom: 1, borderColor: 'divider' } }>
                <Tabs value={ tabValue } onChange={ ( e, val ) => setTabValue( val ) }>
                    <Tab label={ <FormattedMessage id="admin.users-management"/> }/>
                    <Tab label={ <FormattedMessage id="admin.logs"/> }/>
                    <Tab label={ <FormattedMessage id="admin.deleted-books"/> }/>
                </Tabs>
            </Box>
            { tabValue === 0 && <Card className={ styles.cardFullWidth }>
                <CardContent>
                    <UsersGrid/>
                </CardContent>
            </Card> }
            { tabValue === 1 && <Card className={ styles.cardFullWidth }>
                <CardContent>
                    <LogsGrid/>
                </CardContent>
            </Card> }
            { tabValue === 2 && <Card className={ styles.cardFullWidth }>
                <CardContent>
                    <Button className={ "button" } variant={ "contained" } startIcon={ <SettingsBackupRestoreIcon/> }
                            onClick={ () => setRestoreBookDialogVisible( true ) }
                            disabled={ selectedDeletedBookId === null }><FormattedMessage
                        id="admin.deleted-books.revert"/></Button>
                    <BookGrid ref={ deletedBooksGrid } hideButtons={ true } deleted={ true }
                              onBookSelectionChanged={ id => setSelectedDeletedBookId( id ) }/>
                </CardContent>
            </Card> }
        </Box>
    )
}

export default SystemAdmin;
