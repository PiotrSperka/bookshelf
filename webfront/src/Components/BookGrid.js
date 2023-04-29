import "./BookGrid.css"
import {DataGrid} from "@mui/x-data-grid"
import {useEffect, useState} from "react";
import {useGetBooksPage, useGetBooksCount} from "../Services/BooksServiceHook";
import BookFilters from "./BookFilters";
import {Box, Button} from "@mui/material";

const BookGrid = () => {
    const [books, setBooks] = useState([]);
    const cols = [{field: 'author', headerName: 'Author', minWidth: 200, flex: 0.5}, {
        field: 'title',
        headerName: 'Title',
        minWidth: 200,
        flex: 1
    }, {field: 'released', headerName: 'Release', width: 200}, {
        field: 'signature',
        headerName: 'Signature'
    }];
    const [paginationModel, setPaginationModel] = useState({page: 0, pageSize: 15});
    const [rowCountState, setRowCountState] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [filters, setFilters] = useState({});
    const [bookCount, bookCountReady, fetchCount] = useGetBooksCount(filters);
    const [bookPage, bookPageReady, fetchPage] = useGetBooksPage(paginationModel.page, paginationModel.pageSize, filters);

    useEffect(() => {
        if (bookCount) {
            bookCount.clone().text().then(count => {
                setRowCountState(Number(count));
            });
        }
    }, [setRowCountState, paginationModel, bookCount, bookCountReady, bookPageReady])

    useEffect(() => {
        setIsLoading(true);
        if (bookPage) {
            bookPage.clone().json().then(json => {
                setBooks(json);
                setIsLoading(false)
            }).catch(error => {
                console.log(error);
                setIsLoading(false);
            });
        }
    }, [bookPage, bookPageReady, paginationModel]);

    useEffect(() => {
        fetchCount();
        fetchPage();
    }, [fetchCount, fetchPage, filters])

    const filterChanged = filter => {
        setFilters(prevState => ({...prevState, ...filter}));
    }

    const setSortModel = model => {
        if (model.length > 0) {
            const field = (typeof model[0].field === "string") ? model[0].field : "";
            const direction = (typeof model[0].sort === "string") ? model[0].sort : "";
            setFilters(prevState => ({...prevState, sortField: field, sortDirection: direction}));
        } else {
            setFilters(prevState => ({...prevState, sortField: "", sortDirection: ""}));
        }
    }

    return (<div className={"main"}>
        <Box style={{"padding-bottom": "10px"}}>
            <Button className={"button"} variant={"contained"}>Add</Button>
            <Button className={"button"} variant={"outlined"}>Edit</Button>
            <Button className={"button"} variant={"outlined"}>Delete</Button>
            <Button className={"button"} variant={"outlined"}>Refresh</Button>
        </Box>
        <BookFilters onFilterChanged={filterChanged}/>
        <DataGrid className={"dataGrid"} columns={cols} rows={books} autoHeight="true" rowCount={rowCountState}
                  pageSizeOptions={[15]}
                  loading={isLoading}
                  paginationModel={paginationModel}
                  paginationMode="server"
                  onPaginationModelChange={setPaginationModel}
                  onSortModelChange={setSortModel}
                  sortingMode="server"
                  getRowId={(row) => row.remoteId}
                  rowHeight={30}/>
    </div>);
}

export default BookGrid;
