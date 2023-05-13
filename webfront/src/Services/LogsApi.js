const getLogsPageParams = ( pageNumber, pageSize, filters ) => {
    return { url: '/api/logs/page/' + ( pageNumber + 1 ) + '/' + pageSize, method: "POST", data: filters };
}

const getLogsCountParams = filters => {
    return { url: '/api/logs/count', method: "POST", data: filters };
}

export { getLogsPageParams, getLogsCountParams }
