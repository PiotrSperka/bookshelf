import {useApi, useGetRequest, usePostRequest} from "./GenericServiceHook";

const useGetBooksPage = (pageNumber, pageSize, filters) => {
    return usePostRequest('/api/books/page/' + (pageNumber + 1) + '/' + pageSize, filters);
}

const useGetBook = (id) => {
    return useGetRequest('/api/books/id/' + id, null);
}

const useGetBooksCount = (filters) => {
    return usePostRequest('/api/books/count', filters);
}

const useBookApi = () => {
    const getByIdApi = useApi();
    const saveApi = useApi();

    const getById = id => {
        getByIdApi.request('/api/books/id/' + id, 'GET', null);
        return {data: getByIdApi.data, error: getByIdApi.error, loading: getByIdApi.loading};
    }

    const save = (book) => {
        saveApi.request('/api/books/save', 'POST', book);
        return saveApi;
    }

    return {save, getById};
}

export {useGetBooksPage, useGetBooksCount, useGetBook, useBookApi};
