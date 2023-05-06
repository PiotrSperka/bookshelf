const getGetSingleBookParams = id => {
    return { url: "/api/books/id/" + id, method: "GET", data: null };
}

const getSaveBookParams = book => {
    return { url: "/api/books/save", method: "POST", data: book };
}

const getDeleteSingleBookParams = id => {
    return { url: "/api/books/delete/" + id, method: "DELETE", data: null };
}

const getBooksPageParams = ( pageNumber, pageSize, filters ) => {
    return { url: '/api/books/page/' + ( pageNumber + 1 ) + '/' + pageSize, method: "POST", data: filters };
}

const getBooksCountParams = filters => {
    return { url: '/api/books/count', method: "POST", data: filters };
}

export {
    getSaveBookParams,
    getGetSingleBookParams,
    getDeleteSingleBookParams,
    getBooksPageParams,
    getBooksCountParams
};
