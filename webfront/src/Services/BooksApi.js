const getGetSingleBookParams = id => {
    return { url: "/api/books/id/" + id, method: "GET", data: null };
}

const getSaveBookParams = book => {
    return { url: "/api/books/save", method: "POST", data: book };
}

const getDeleteSingleBookParams = id => {
    return { url: "/api/books/delete/" + id, method: "DELETE", data: null };
}

const getRestoreBookParams = id => {
    return { url: "/api/books/restore/" + id, method: "POST", data: null };
}

const getBooksPageParams = ( pageNumber, pageSize, filters, deleted = false ) => {
    if ( deleted === true ) {
        return { url: '/api/books/deleted/page/' + ( pageNumber + 1 ) + '/' + pageSize, method: "POST", data: filters };
    } else {
        return { url: '/api/books/page/' + ( pageNumber + 1 ) + '/' + pageSize, method: "POST", data: filters };
    }
}

const getBooksCountParams = ( filters, deleted = false ) => {
    if ( deleted === true ) {
        return { url: '/api/books/deleted/count', method: "POST", data: filters };
    } else {
        return { url: '/api/books/count', method: "POST", data: filters };
    }
}

export {
    getSaveBookParams,
    getGetSingleBookParams,
    getRestoreBookParams,
    getDeleteSingleBookParams,
    getBooksPageParams,
    getBooksCountParams
};
