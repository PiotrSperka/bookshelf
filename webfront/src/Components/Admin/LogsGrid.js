import styles from "./LogsGrid.module.css"
import { useEffect, useState } from "react";
import { useApi } from "../../Services/GenericServiceHook";
import { DataGrid, enUS, plPL } from "@mui/x-data-grid";
import { useIntl } from "react-intl";
import { useUserContext } from "../../UserContextProvider";
import { getLogsCountParams, getLogsPageParams } from "../../Services/LogsApi";

const LogsGrid = () => {
    const intl = useIntl();
    const logsCols = [ {
        field: 'username',
        headerName: intl.formatMessage( { id: "admin.logs.name" } ),
        minWidth: 200,
        flex: 0.25
    }, {
        field: 'date',
        headerName: intl.formatMessage( { id: "admin.logs.date" } ),
        minWidth: 200,
        flex: 0.25,
        renderCell: params => {
            const d = new Date( params.row[ params.field ] );
            return d.getFullYear() + '-' + ( d.getMonth() + 1 ).toString().padStart( 2, '0' ) + '-' + d.getDate().toString().padStart( 2, '0' ) + ' '
                + d.getHours().toString().padStart( 2, '0' ) + ':' + d.getMinutes().toString().padStart( 2, '0' ) + ':' + d.getSeconds().toString().padStart( 2, '0' );
        }
    }, {
        field: 'log',
        headerName: intl.formatMessage( { id: "admin.logs.log" } ),
        minWidth: 200,
        flex: 1
    } ];

    const [ logs, setLogs ] = useState( [] );
    const [ paginationModel, setPaginationModel ] = useState( { page: 0, pageSize: 15 } );
    const [ rowCount, setRowCount ] = useState( 0 );
    const [ isLoading, setIsLoading ] = useState( false );
    const [ filters, setFilters ] = useState( { sortField: "date", sortDirection: "desc" } );

    const getLogsPageApi = useApi();
    const getLogsCountApi = useApi();

    const { isLoggedIn } = useUserContext();

    const locale = navigator.language.split( /[-_]/ )[ 0 ];
    const dataGridLocalization = locale === 'pl' ? plPL.components.MuiDataGrid.defaultProps.localeText : enUS.components.MuiDataGrid.defaultProps.localeText;

    useEffect( () => {
        if ( getLogsCountApi.data ) {
            getLogsCountApi.data.clone().text().then( count => {
                setRowCount( Number( count ) );
            } );
        }
    }, [ getLogsCountApi.data ] )

    useEffect( () => {
        if ( getLogsPageApi.data ) {
            setIsLoading( true );
            getLogsPageApi.data.clone().json().then( json => {
                setLogs( json );
                setIsLoading( false )
            } ).catch( error => {
                console.log( error );
                setIsLoading( false );
            } );
        }
    }, [ getLogsPageApi.data ] );

    useEffect( () => {
        loadBooks()
    }, [ paginationModel.page, paginationModel.pageSize, filters, isLoggedIn ] )

    const loadBooks = () => {
        if ( isLoggedIn() ) {
            getLogsPageApi.request( getLogsPageParams( paginationModel.page, paginationModel.pageSize, filters ) )
            getLogsCountApi.request( getLogsCountParams( filters ) )
        }
    }

    const setSortModel = model => {
        if ( model.length > 0 ) {
            const field = ( typeof model[ 0 ].field === "string" ) ? model[ 0 ].field : "";
            const direction = ( typeof model[ 0 ].sort === "string" ) ? model[ 0 ].sort : "";
            setFilters( prevState => ( { ...prevState, sortField: field, sortDirection: direction } ) );
        } else {
            setFilters( prevState => ( { ...prevState, sortField: "", sortDirection: "" } ) );
        }
    }

    return (
        <DataGrid className={ styles.dataGrid }
                  columns={ logsCols }
                  rows={ logs }
                  autoHeight={ true }
                  rowCount={ rowCount }
                  getRowId={ ( row ) => row.id }
                  localeText={ dataGridLocalization }
                  initialState={ {
                      sorting: {
                          sortModel: [ { field: 'date', sort: 'desc' } ],
                      },
                  } }
                  pageSizeOptions={ [ 15 ] }
                  loading={ isLoading }
                  disableMultipleRowSelection={ true }
                  disableRowSelectionOnClick={ true }
                  disableColumnSelector={ true }
                  paginationModel={ paginationModel }
                  paginationMode="server"
                  onPaginationModelChange={ setPaginationModel }
                  onSortModelChange={ setSortModel }
                  sortingMode="server"
                  getRowHeight={ () => 'auto' }
                  density="compact"
                  sx={ {
                      '&.MuiDataGrid-root--densityCompact .MuiDataGrid-cell': { py: '8px' },
                      '&.MuiDataGrid-root--densityStandard .MuiDataGrid-cell': { py: '15px' },
                      '&.MuiDataGrid-root--densityComfortable .MuiDataGrid-cell': { py: '22px' },
                  } }
                  disableColumnMenu={ true }/>
    )
}

export default LogsGrid;
