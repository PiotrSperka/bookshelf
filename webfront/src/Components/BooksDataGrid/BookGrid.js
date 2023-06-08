import styles from "./BookGrid.module.css"
import { DataGrid, enUS, plPL } from "@mui/x-data-grid"
import { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import BookFilters from "./BookFilters";
import { useIntl } from "react-intl";
import { useApi } from "../../Services/GenericServiceHook";
import { getBooksCountParams, getBooksPageParams } from "../../Services/BooksApi";
import { useUserContext } from "../../UserContextProvider";
import BookGridButtons from "./BookGridButtons";

const BookGrid = forwardRef( ( props, ref ) => {
    useImperativeHandle( ref, () => ( {
        refresh() {
            loadBooks();
        }
    } ) );

    const intl = useIntl();
    const [ books, setBooks ] = useState( [] );
    const cols = [ {
        field: 'author',
        headerName: intl.formatMessage( { id: "book-grid.author" } ),
        minWidth: 200,
        flex: 0.5
    }, {
        field: 'title',
        headerName: intl.formatMessage( { id: "book-grid.title" } ),
        minWidth: 200,
        flex: 1
    }, { field: 'released', headerName: intl.formatMessage( { id: "book-grid.release" } ), width: 200 }, {
        field: 'signature',
        headerName: intl.formatMessage( { id: "book-grid.signature" } )
    } ];

    const [ paginationModel, setPaginationModel ] = useState( { page: 0, pageSize: 15 } );
    const [ rowCountState, setRowCountState ] = useState( 0 );
    const [ isLoading, setIsLoading ] = useState( false );
    const [ filters, setFilters ] = useState( {
        sortField: "author",
        sortDirection: "asc",
        author: "",
        title: "",
        release: "",
        signature: ""
    } );

    const getBooksPageApi = useApi();
    const getBooksCountApi = useApi();

    const [ selectedBookId, setSelectedBookId ] = useState( null );
    const [ rowDoubleClickTrigger, setRowDoubleClickTrigger ] = useState( 0 );

    const { isLoggedIn } = useUserContext();

    const locale = navigator.language.split( /[-_]/ )[ 0 ];
    const dataGridLocalization = locale === 'pl' ? plPL.components.MuiDataGrid.defaultProps.localeText : enUS.components.MuiDataGrid.defaultProps.localeText;

    useEffect( () => {
        if ( getBooksCountApi.data ) {
            getBooksCountApi.data.clone().text().then( count => {
                setRowCountState( Number( count ) );
            } );
        }
    }, [ getBooksCountApi.data ] )

    useEffect( () => {
        if ( getBooksPageApi.data ) {
            setIsLoading( true );
            getBooksPageApi.data.clone().json().then( json => {
                setBooks( json );
                setIsLoading( false )
            } ).catch( error => {
                console.log( error );
                setIsLoading( false );
            } );
        }
    }, [ getBooksPageApi.data ] );

    useEffect( () => {
        loadBooks()
    }, [ paginationModel.page, paginationModel.pageSize, filters.sortField, filters.sortDirection, filters.author, filters.title, filters.release, filters.signature, isLoggedIn ] )

    const filterChanged = filter => {
        setFilters( prevState => ( { ...prevState, ...filter } ) );
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

    const rowSelectionChanged = model => {
        const selectedId = model.length > 0 ? model[ 0 ] : null;
        setSelectedBookId( selectedId );
        if ( props.onBookSelectionChanged !== undefined && props.onBookSelectionChanged !== null ) {
            props.onBookSelectionChanged( selectedId );
        }
    }

    const rowDoubleClicked = () => {
        setRowDoubleClickTrigger( prevState => prevState + 1 );
    }

    const loadBooks = () => {
        if ( isLoggedIn() ) {
            getBooksPageApi.request( getBooksPageParams( paginationModel.page, paginationModel.pageSize, filters, props.deleted ) )
            getBooksCountApi.request( getBooksCountParams( filters, props.deleted ) )
        } else {
            setBooks( [] );
            setRowCountState( 0 );
        }
    }

    return ( <div className={ styles.main }>
        { props.hideButtons !== true &&
            <BookGridButtons selectedBookId={ selectedBookId } onBooksChanged={ loadBooks }
                             editTrigger={ rowDoubleClickTrigger }/> }
        <BookFilters onFilterChanged={ filterChanged }/>
        <DataGrid className={ styles.dataGrid } columns={ cols } rows={ books } autoHeight={ true }
                  rowCount={ rowCountState }
                  localeText={ dataGridLocalization }
                  initialState={ {
                      sorting: {
                          sortModel: [ { field: 'author', sort: 'asc' } ],
                      },
                  } }
                  pageSizeOptions={ [ 15 ] }
                  loading={ isLoading }
                  disableMultipleRowSelection={ true }
                  onRowSelectionModelChange={ rowSelectionChanged }
                  paginationModel={ paginationModel }
                  paginationMode="server"
                  onPaginationModelChange={ setPaginationModel }
                  onSortModelChange={ setSortModel }
                  onRowDoubleClick={ rowDoubleClicked }
                  sortingMode="server"
                  getRowId={ ( row ) => row.remoteId }
                  getRowHeight={ () => 'auto' }
                  density="compact"
                  sx={ {
                      '&.MuiDataGrid-root--densityCompact .MuiDataGrid-cell': { py: '8px' },
                      '&.MuiDataGrid-root--densityStandard .MuiDataGrid-cell': { py: '15px' },
                      '&.MuiDataGrid-root--densityComfortable .MuiDataGrid-cell': { py: '22px' },
                  } }
                  disableColumnMenu={ true }/>
    </div> );
} );

BookGrid.defaultProps = {
    hideButtons: false,
    deleted: false,
}

export default BookGrid;
