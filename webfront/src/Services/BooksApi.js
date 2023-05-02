const getGetSingleBookParams = id => {
    return {url: "/books/id/" + id, method: "GET", data: null};
}

const getSaveBookParams = book => {
    return {url: "/books/save", method: "POST", data: book};
}

const getDeleteSingleBookParams = id => {
    return {url: "/books/delete/" + id, method: "DELETE", data: null};
}

export {getSaveBookParams, getGetSingleBookParams, getDeleteSingleBookParams};
