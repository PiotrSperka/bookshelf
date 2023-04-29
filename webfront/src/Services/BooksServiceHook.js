import {usePostRequest} from "./GenericServiceHook";

const useGetBooksPage = (pageNumber, pageSize, filters) => {
    return usePostRequest('/books/page/' + (pageNumber + 1) + '/' + pageSize, filters);
}

const useGetBooksCount = (filters) => {
    return usePostRequest('/books/count', filters);
}

export {useGetBooksPage, useGetBooksCount};
