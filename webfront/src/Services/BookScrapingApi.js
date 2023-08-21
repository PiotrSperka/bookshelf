const getAllJobsParams = () => {
    return { url: "/api/bookscraping/job", method: "GET", data: null };
}

const getAddJobParams = job => {
    return { url: "/api/bookscraping/job", method: "POST", data: job };
}

const getDownloadResultParams = id => {
    return { url: "/api/bookscraping/result/" + id, method: "GET", data: null };
}

export {
    getAllJobsParams,
    getAddJobParams,
    getDownloadResultParams
};