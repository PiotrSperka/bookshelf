import styles from "./SystemAdmin.module.css"
import { Box, Button, Card, CardContent, Chip, Typography } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import AddIcon from "@mui/icons-material/Add";
import { useApi } from "../Services/GenericServiceHook";
import { useEffect, useState } from "react";
import { getAllUsersParams } from "../Services/UserApi";
import AddEditUser from "./AddEditUser";
import DeleteUser from "./DeleteUser";
import { useUserContext } from "../UserContextProvider";
import { useNavigate } from "react-router-dom";

const SystemAdmin = () => {
    const cols = [ { field: 'name', headerName: 'Name', minWidth: 200, flex: 0.5 }, {
        field: 'roles',
        headerName: 'Roles',
        minWidth: 200,
        flex: 1,
        renderCell: params => {
            if (params.row[ params.field ] instanceof Array) {
                return params.row[ params.field ].map( ( role) => <Chip label={ role } color={"primary"} sx={{marginRight: "3px"}}/> );
            }
        }
    }, {
        field: 'isActive', headerName: 'Active', width: 100, renderCell: params => {
            if ( params.row[ params.field ] === true ) {
                return ( <Chip label="Enabled" color="success"/> )
            } else {
                return ( <Chip label="Disabled" color="error"/> )
            }
        }
    }, {
        field: 'change-settings',
        headerName: '',
        width: 150,
        renderCell: ( params ) => {
            const onClick = () => {
                setEditUserId(params.row['id'])
                setUserDialogVisible(true)
            }
            return ( <Button className={ "button" } variant={ "contained" } onClick={onClick}>Settings</Button> )
        }
    }, {
        field: 'delete',
        headerName: '',
        width: 150,
        renderCell: ( params ) => {
            const onClick = () => {
                setEditUserId(params.row['id'])
                setDeleteDialogVisible(true)
            }
            return ( <Button className={ "button" } variant={ "contained" } color={ "error" } onClick={onClick} disabled={user.id === params.row['id']}>Delete</Button> )
        }
    } ];

    const { user, hasRole, isLoggedIn } = useUserContext();
    const navigate = useNavigate();
    const getUsersApi = useApi();
    const deleteUserApi = useApi();
    const [editUserId, setEditUserId] = useState(null);
    const [userDialogVisible, setUserDialogVisible] = useState(false);
    const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
    const [ users, setUsers ] = useState( [] );

    useEffect( () => {
        if (!isLoggedIn || !hasRole("admin")) {
            navigate("/")
        }
        getUsersApi.request( getAllUsersParams() );
    }, [isLoggedIn] )

    useEffect( () => {
        if ( getUsersApi.data != null && getUsersApi.data.status === 200 ) {
            getUsersApi.data.clone().json().then( json => {
                setUsers( json );
            } );
        }
    }, [ getUsersApi.data ] )

    useEffect( () => {
        getUsersApi.request( getAllUsersParams() );
    }, [ deleteUserApi.data ] )

    const onUserDialogClose = () => {
        setUserDialogVisible(false);
        getUsersApi.request( getAllUsersParams() );
    }

    const onDeleteDialogClose = () => {
        setDeleteDialogVisible(false);
        getUsersApi.request( getAllUsersParams() );
    }

    const onAddUserClick = () => {
        setEditUserId(null);
        setUserDialogVisible(true);
    }

    return (
        <Box className={ styles.main }>
            <AddEditUser userId={editUserId} open={userDialogVisible} onClose={onUserDialogClose} />
            <DeleteUser userId={editUserId} open={deleteDialogVisible} onClose={onDeleteDialogClose} />
            <Card className={ styles.cardFullWidth }>
                <CardContent>
                    <Typography gutterBottom variant={ "h5" } component={ "div" }>
                        Users management
                    </Typography>
                    <Button className={ "button" } variant={ "contained" } startIcon={ <AddIcon/> } onClick={onAddUserClick}>Add user</Button>
                    <DataGrid className={ styles.dataGrid } columns={ cols } rows={ users } autoHeight={true}
                              rowCount={ users.length } getRowId={(row) => row.id}/>
                </CardContent>
            </Card>
            {false && <Card className={ styles.cardFullWidth }>
                <CardContent>
                    <Typography gutterBottom variant={ "h5" } component={ "div" }>
                        Deleted books
                    </Typography>
                    <DataGrid className={ styles.dataGrid } columns={ cols }
                              rows={ [ { id: 1, username: "user", roles: ["user"], active: true } ] }
                              autoHeight={true} rowCount={ 1 } getRowId={(row) => row.id}/>
                </CardContent>
            </Card>}
        </Box>
    )
}

export default SystemAdmin;
