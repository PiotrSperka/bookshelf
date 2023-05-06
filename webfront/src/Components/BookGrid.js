import styles from "./BookGrid.module.css"
import { DataGrid, plPL, enUS } from "@mui/x-data-grid"
import { useEffect, useState } from "react";
import BookFilters from "./BookFilters";
import { Box, Button } from "@mui/material";
import AddEditBook from "./AddEditBook";
import DeleteBook from "./DeleteBook";
import AddIcon from "@mui/icons-material/Add"
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import RefreshIcon from '@mui/icons-material/Refresh';
import { FormattedMessage } from "react-intl";
import { useApi } from "../Services/GenericServiceHook";
import { getBooksCountParams, getBooksPageParams } from "../Services/BooksApi";

const BookGrid = () => {
    const [ books, setBooks ] = useState( [] );
    const cols = [ {
        field: 'author',
        headerName: <FormattedMessage id="book-grid.author"/>,
        minWidth: 200,
        flex: 0.5
    }, {
        field: 'title',
        headerName: <FormattedMessage id="book-grid.title"/>,
        minWidth: 200,
        flex: 1
    }, { field: 'released', headerName: <FormattedMessage id="book-grid.release"/>, width: 200 }, {
        field: 'signature',
        headerName: <FormattedMessage id="book-grid.signature"/>
    } ];

    const [ paginationModel, setPaginationModel ] = useState( { page: 0, pageSize: 15 } );
    const [ rowCountState, setRowCountState ] = useState( 0 );
    const [ isLoading, setIsLoading ] = useState( false );
    const [ filters, setFilters ] = useState( { sortField: "author", sortDirection: "asc" } );

    const getBooksPageApi = useApi();
    const getBooksCountApi = useApi();

    const [ addDialogOpen, setAddDialogOpen ] = useState( false );
    const [ editDialogOpen, setEditDialogOpen ] = useState( false );
    const [ deleteDialogOpen, setDeleteDialogOpen ] = useState( false );
    const [ selectedBookId, setSelectedBookId ] = useState( null );

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
        getBooksPageApi.request( getBooksPageParams( paginationModel.page, paginationModel.pageSize, filters ) )
        getBooksCountApi.request( getBooksCountParams( filters ) )
    }, [ paginationModel.page, paginationModel.pageSize, filters ] )

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
        if ( model.length > 0 ) {
            setSelectedBookId( model[ 0 ] );
        } else {
            setSelectedBookId( null );
        }
    }

    const handleBooksChanged = refresh => {
        setAddDialogOpen( false );
        setEditDialogOpen( false );
        setDeleteDialogOpen( false );
        if ( refresh === true ) {
            getBooksPageApi.request( getBooksPageParams( paginationModel.page, paginationModel.pageSize, filters ) )
            getBooksCountApi.request( getBooksCountParams( filters ) )
        }
    }

    return ( <div className={ styles.main }>
        <AddEditBook open={ addDialogOpen } bookId={ null } onClose={ handleBooksChanged }/>
        <AddEditBook open={ editDialogOpen } bookId={ selectedBookId } onClose={ handleBooksChanged }/>
        <DeleteBook open={ deleteDialogOpen } bookId={ selectedBookId } onClose={ handleBooksChanged }/>
        <Box style={ { "padding-bottom": "10px" } }>
            <Button className={ styles.button } variant={ "contained" } startIcon={ <AddIcon/> } onClick={ () => {
                setAddDialogOpen( true );
            } }><FormattedMessage id="book-grid.add"/></Button>
            <Button className={ styles.button } variant={ "outlined" } startIcon={ <EditIcon/> }
                    disabled={ selectedBookId === null } onClick={ () => {
                setEditDialogOpen( true );
            } }><FormattedMessage id="book-grid.edit"/></Button>
            <Button className={ styles.button } variant={ "outlined" } startIcon={ <DeleteIcon/> } color={ "error" }
                    disabled={ selectedBookId === null } onClick={ () => setDeleteDialogOpen( true ) }><FormattedMessage
                id="book-grid.delete"/></Button>
            <Button className={ styles.button } variant={ "outlined" } startIcon={ <RefreshIcon/> } onClick={() => handleBooksChanged(true)}><FormattedMessage
                id="book-grid.refresh"/></Button>
        </Box>
        <BookFilters onFilterChanged={ filterChanged }/>
        <DataGrid className={ styles.dataGrid } columns={ cols } rows={ books } autoHeight="true"
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
                  sortingMode="server"
                  getRowId={ ( row ) => row.remoteId }
                  rowHeight={ 30 }
                  disableColumnMenu={ true }/>
    </div> );
}

export default BookGrid;
