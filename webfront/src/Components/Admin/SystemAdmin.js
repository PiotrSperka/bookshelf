import styles from "./SystemAdmin.module.css"
import { Box, Button, Card, CardContent, Chip, Tab, Tabs } from "@mui/material";
import { DataGrid, enUS, plPL } from "@mui/x-data-grid";
import AddIcon from "@mui/icons-material/Add";
import SettingsBackupRestoreIcon from '@mui/icons-material/SettingsBackupRestore';
import { useApi } from "../../Services/GenericServiceHook";
import { useEffect, useRef, useState } from "react";
import { getAllUsersParams } from "../../Services/UserApi";
import AddEditUser from "./AddEditUser";
import DeleteUser from "./DeleteUser";
import { useUserContext } from "../../UserContextProvider";
import { useNavigate } from "react-router-dom";
import { FormattedMessage, useIntl } from "react-intl";
import LogsGrid from "./LogsGrid";
import BookGrid from "../BooksDataGrid/BookGrid";
import RestoreBook from "./RestoreBook";

const SystemAdmin = () => {
    const intl = useIntl();
    const cols = [ {
        field: 'name',
        headerName: intl.formatMessage( { id: "admin.users-management.name" } ),
        minWidth: 200,
        flex: 1
    }, {
        field: 'roles',
        headerName: intl.formatMessage( { id: "admin.users-management.roles" } ),
        minWidth: 200,
        flex: 1,
        renderCell: params => {
            if ( params.row[ params.field ] instanceof Array ) {
                return params.row[ params.field ].map( ( role ) => <Chip key={ role } label={ role } color={ "primary" }
                                                                         sx={ { marginRight: "3px" } }/> );
            }
        }
    }, {
        field: 'active',
        headerName: intl.formatMessage( { id: "admin.users-management.active" } ),
        width: 150,
        renderCell: params => {
            if ( params.row[ params.field ] === true ) {
                return ( <Chip label={ <FormattedMessage id="admin.users-management.enabled"/> } color="success"/> )
            } else {
                return ( <Chip label={ <FormattedMessage id="admin.users-management.disabled"/> } color="error"/> )
            }
        }
    }, {
        field: 'change-settings',
        headerName: '',
        width: 150,
        sortable: false,
        renderCell: ( params ) => {
            const onClick = () => {
                setEditUserId( params.row[ 'id' ] )
                setUserDialogVisible( true )
            }
            return ( <Button className={ "button" } variant={ "contained" } onClick={ onClick }><FormattedMessage
                id="admin.users-management.settings"/></Button> )
        }
    }, {
        field: 'delete',
        headerName: '',
        width: 150,
        sortable: false,
        renderCell: ( params ) => {
            const onClick = () => {
                setEditUserId( params.row[ 'id' ] )
                setDeleteDialogVisible( true )
            }
            return ( <Button className={ "button" } variant={ "contained" } color={ "error" } onClick={ onClick }
                             disabled={ user.id === params.row[ 'id' ] }><FormattedMessage
                id="admin.users-management.delete"/></Button> )
        }
    } ];

    const { user, hasRole, isLoggedIn } = useUserContext();
    const navigate = useNavigate();
    const getUsersApi = useApi();
    const [ editUserId, setEditUserId ] = useState( null );
    const [ userDialogVisible, setUserDialogVisible ] = useState( false );
    const [ deleteDialogVisible, setDeleteDialogVisible ] = useState( false );
    const [ users, setUsers ] = useState( [] );
    const [ selectedDeletedBookId, setSelectedDeletedBookId ] = useState( null );
    const [ restoreBookDialogVisible, setRestoreBookDialogVisible ] = useState( false );

    const deletedBooksGrid = useRef();

    const [ tabValue, setTabValue ] = useState( 0 );

    const locale = navigator.language.split( /[-_]/ )[ 0 ];
    const dataGridLocalization = locale === 'pl' ? plPL.components.MuiDataGrid.defaultProps.localeText : enUS.components.MuiDataGrid.defaultProps.localeText;

    useEffect( () => {
        if ( !isLoggedIn || !hasRole( "admin" ) ) {
            navigate( "/" )
        }
        getUsersApi.request( getAllUsersParams() );
    }, [ isLoggedIn ] )

    useEffect( () => {
        if ( getUsersApi.data != null && getUsersApi.data.status === 200 ) {
            getUsersApi.data.clone().json().then( json => {
                setUsers( json );
            } );
        }
    }, [ getUsersApi.data ] )

    const onUserDialogClose = () => {
        setUserDialogVisible( false );
        getUsersApi.request( getAllUsersParams() );
    }

    const onDeleteDialogClose = () => {
        setDeleteDialogVisible( false );
        getUsersApi.request( getAllUsersParams() );
    }

    const onAddUserClick = () => {
        setEditUserId( null );
        setUserDialogVisible( true );
    }

    const onRevertDialogClose = () => {
        setRestoreBookDialogVisible( false );
        if ( deletedBooksGrid.current !== undefined ) {
            deletedBooksGrid.current.refresh();
        }
    }

    return (
        <Box className={ styles.main }>
            <AddEditUser userId={ editUserId } open={ userDialogVisible } onClose={ onUserDialogClose }/>
            <DeleteUser userId={ editUserId } open={ deleteDialogVisible } onClose={ onDeleteDialogClose }/>
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
                    <Button className={ "button" } variant={ "contained" } startIcon={ <AddIcon/> }
                            onClick={ onAddUserClick }><FormattedMessage id="admin.users-management.add-user"/></Button>
                    <DataGrid className={ styles.dataGrid } columns={ cols } rows={ users } autoHeight={ true }
                              localeText={ dataGridLocalization } disableColumnMenu={ true }
                              rowCount={ users.length } getRowId={ ( row ) => row.id }/>
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
