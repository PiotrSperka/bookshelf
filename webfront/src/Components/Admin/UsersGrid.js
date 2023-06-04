import styles from "./UsersGrid.module.css"
import { DataGrid, enUS, plPL } from "@mui/x-data-grid";
import { Box, Button, Chip } from "@mui/material";
import { FormattedMessage, useIntl } from "react-intl";
import AddIcon from "@mui/icons-material/Add";
import AddEditUser from "./AddEditUser";
import DeleteUser from "./DeleteUser";
import { useEffect, useState } from "react";
import { useUserContext } from "../../UserContextProvider";
import { getAllUsersParams } from "../../Services/UserApi";
import { useApi } from "../../Services/GenericServiceHook";

const UsersGrid = props => {
    const intl = useIntl();
    const getUsersApi = useApi();
    const { user } = useUserContext();
    const [ editUserId, setEditUserId ] = useState( null );
    const [ userDialogVisible, setUserDialogVisible ] = useState( false );
    const [ deleteDialogVisible, setDeleteDialogVisible ] = useState( false );
    const [ users, setUsers ] = useState( [] );

    const locale = navigator.language.split( /[-_]/ )[ 0 ];
    const dataGridLocalization = locale === 'pl' ? plPL.components.MuiDataGrid.defaultProps.localeText : enUS.components.MuiDataGrid.defaultProps.localeText;

    const cols = [ {
        field: 'name',
        headerName: intl.formatMessage( { id: "admin.users-management.name" } ),
        minWidth: 200,
        flex: 1
    }, {
        field: 'email',
        headerName: intl.formatMessage( { id: "admin.users-management.email" } ),
        minWidth: 200,
        flex: 1
    }, {
        field: 'locale',
        headerName: intl.formatMessage( { id: "admin.users-management.locale" } ),
        minWidth: 100,
        flex: 0.5
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

    const onUserDialogClose = refresh => {
        setUserDialogVisible( false );
        if ( refresh === true ) {
            getUsersApi.request( getAllUsersParams() );
        }
    }

    const onDeleteDialogClose = ( refresh ) => {
        setDeleteDialogVisible( false );
        if ( refresh === true ) {
            getUsersApi.request( getAllUsersParams() );
        }
    }

    const onAddUserClick = () => {
        setEditUserId( null );
        setUserDialogVisible( true );
    }

    useEffect( () => {
        if ( getUsersApi.data != null && getUsersApi.data.status === 200 ) {
            getUsersApi.data.clone().json().then( json => {
                setUsers( json );
            } );
        }
    }, [ getUsersApi.data ] )

    useEffect( () => {
        getUsersApi.request( getAllUsersParams() );
    }, [] )

    return (
        <Box>
            <AddEditUser userId={ editUserId } open={ userDialogVisible } onClose={ onUserDialogClose }/>
            <DeleteUser userId={ editUserId } open={ deleteDialogVisible } onClose={ onDeleteDialogClose }/>
            <Button className={ "button" } variant={ "contained" } startIcon={ <AddIcon/> }
                    onClick={ onAddUserClick }><FormattedMessage id="admin.users-management.add-user"/></Button>
            <DataGrid className={ styles.dataGrid } columns={ cols } rows={ users } autoHeight={ true }
                      localeText={ dataGridLocalization } disableColumnMenu={ true }
                      rowCount={ users.length } getRowId={ ( row ) => row.id }/>
        </Box>
    )
}

export default UsersGrid;
