import { Box, Button, Chip } from "@mui/material";
import styles from "../Admin/UsersGrid.module.css";
import { DataGrid, enUS, plPL } from "@mui/x-data-grid";
import { FormattedMessage, useIntl } from "react-intl";
import { useEffect, useState } from "react";
import { useApi } from "../../Services/GenericServiceHook";
import { getAllJobsParams, getDownloadResultParams } from "../../Services/BookScrapingApi";
import { useUserContext } from "../../UserContextProvider";
import DownloadIcon from "@mui/icons-material/Download";

const ScrapedBooksGrid = props => {
    const intl = useIntl();
    const getJobsApi = useApi();
    const getResultApi = useApi();
    const { isLoggedIn } = useUserContext();
    const [ jobs, setJobs ] = useState( [] );
    const [ downloadTitle, setDownloadTitle ] = useState( "" );
    const [ isLoading, setIsLoading ] = useState( false );

    const locale = navigator.language.split( /[-_]/ )[ 0 ];
    const dataGridLocalization = locale === 'pl' ? plPL.components.MuiDataGrid.defaultProps.localeText : enUS.components.MuiDataGrid.defaultProps.localeText;

    const cols = [ {
        field: 'title',
        headerName: intl.formatMessage( { id: "book-scraping.title" } ),
        minWidth: 200,
        flex: 1
    }, {
        field: 'inputData',
        headerName: intl.formatMessage( { id: "book-scraping.link" } ),
        minWidth: 200,
        flex: 1
    }, {
        field: 'bookScrapingState',
        headerName: intl.formatMessage( { id: "book-scraping.state" } ),
        width: 150,
        renderCell: params => {
            if ( params.row[ params.field ] === "QUEUED" ) {
                return ( <Chip label={ <FormattedMessage id="book-scraping.queued"/> } color="default"/> )
            } else if ( params.row[ params.field ] === "PROCESSING" ) {
                return ( <Chip label={ <FormattedMessage id="book-scraping.processing"/> } color="info"/> )
            } else if ( params.row[ params.field ] === "READY" ) {
                return ( <Chip label={ <FormattedMessage id="book-scraping.ready"/> } color="success"/> )
            } else {
                return ( <Chip label={ <FormattedMessage id="book-scraping.error"/> } color="error"/> )
            }
        }
    }, {
        field: 'download',
        headerName: '',
        width: 150,
        sortable: false,
        renderCell: ( params ) => {
            const onClick = () => {
                setDownloadTitle( params.row[ "title" ] )
                setIsLoading( true )
                getResultApi.request( getDownloadResultParams( params.row[ "id" ] ) )
            }
            return ( params.row[ "bookScrapingState" ] === "READY" &&
                <Button className={ "button" } variant={ "contained" } startIcon={ <DownloadIcon/> }
                        onClick={ onClick }><FormattedMessage
                    id="book-scraping.download"/></Button> )
        }
    } ];

    const refreshList = () => {
        getJobsApi.request( getAllJobsParams() )
    }

    useEffect( () => {
        const interval = setInterval( () => refreshList(), 10000 );
        return () => clearInterval( interval );
    }, [] )

    useEffect( () => {
        refreshList()
    }, [ isLoggedIn, props.refreshTrigger ] )

    useEffect( () => {
        if ( getJobsApi.data ) {
            getJobsApi.data.clone().json().then( json => {
                setJobs( json );
            } ).catch( error => {
                console.log( error );
            } );
        }
    }, [ getJobsApi.data ] );

    useEffect( () => {
        if ( getResultApi.data ) {
            getResultApi.data.blob().then( blob => {
                const objectURL = URL.createObjectURL( blob );
                const tempLink = document.createElement( 'a' );
                tempLink.href = objectURL;
                tempLink.setAttribute( 'download', downloadTitle + '.pdf' );
                tempLink.click();
                setIsLoading( false );
                URL.revokeObjectURL( objectURL );
            } ).catch( error => {
                console.log( error );
                setIsLoading( false );
            } );
        }
    }, [ getResultApi.data ] );

    return (
        <Box>
            <DataGrid className={ styles.dataGrid } columns={ cols } rows={ jobs } autoHeight={ true }
                      localeText={ dataGridLocalization } disableColumnMenu={ true } loading={ isLoading }
                      rowCount={ jobs.length } getRowId={ ( row ) => row.id }/>
        </Box>
    )
}

export default ScrapedBooksGrid;
