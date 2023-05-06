const getGetSingleBookParams = id => {
    return {url: "/api/books/id/" + id, method: "GET", data: null};
}

const getSaveBookParams = book => {
    return {url: "/api/books/save", method: "POST", data: book};
}

const getDeleteSingleBookParams = id => {
    return {url: "/api/books/delete/" + id, method: "DELETE", data: null};
}

export {getSaveBookParams, getGetSingleBookParams, getDeleteSingleBookParams};
