import {useCallback, useEffect, useState} from "react";
import {useUserContext} from "../UserContextProvider";

const getHeaders = (userContext) => {
    const token = userContext.token;
    if (token) {
        return {"Content-Type": "application/json", "Authorization": "Bearer " + token};
    } else {
        return {"Content-Type": "application/json"};
    }
}

const useReceive = (url, method, data) => {
    const {user, logout, isLoggedIn} = useUserContext();
    const [res, setRes] = useState(null);
    const [ready, setReady] = useState(false);

    const fetchData = useCallback( async () => {
        setReady(false);
        const response = await fetch(url, {
            method: method,
            headers: getHeaders(user),
            body: (data) ? JSON.stringify(data) : null
        });
        if (response.status === 401) {
            if (isLoggedIn()) {
                logout() // clear token, as it is invalid now
            }
            // navigate("/login");
        } else {
            setRes(response);
            setReady(true);
        }
    }, [data, isLoggedIn, logout, method, url, user] );

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    return [res, ready, fetchData];
}

const useGetRequest = (url) => {
    return useReceive(url, "GET", null);
}

const usePostRequest = (url, data) => {
    return useReceive(url, "POST", data);
}

export {useGetRequest, usePostRequest};
