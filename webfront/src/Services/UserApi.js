const getUserInfoParams = () => {
    return { url: "/api/user/info", method: "GET", data: null };
}

const getAllUsersParams = () => {
    return { url: "/api/user/all", method: "GET", data: null };
}

const getUserParams = id => {
    return { url: "/api/user/id/" + id, method: "GET", data: null };
}

const deleteUserParams = id => {
    return { url: "/api/user/id/" + id, method: "DELETE", data: null };
}

const editUserParams = data => {
    return { url: "/api/user/edit", method: "POST", data: data };
}

const addUserParams = data => {
    return { url: "/api/user/add", method: "POST", data: data };
}

const changeUserPasswordParams = data => {
    return { url: "/api/user/change-password", method: "POST", data: data };
}

const logoutParams = () => {
    return { url: "/api/auth/logout", method: "GET", data: null };
}

const getResetPasswordParams = ( token, data ) => {
    return { url: "/api/user/reset-password/" + token, method: "POST", data: data };
}

const getRequestResetPasswordParams = ( data ) => {
    return { url: "/api/user/request-reset-password", method: "POST", data: data };
}

export {
    getUserInfoParams,
    changeUserPasswordParams,
    getAllUsersParams,
    getUserParams,
    editUserParams,
    addUserParams,
    deleteUserParams,
    logoutParams,
    getResetPasswordParams,
    getRequestResetPasswordParams
};

