
const getUserInfoParams = () => {
    return {url: "/user/info", method: "GET", data: null};
}

const getAllUsersParams = () => {
    return {url: "/user/all", method: "GET", data: null};
}

const getUserParams = id => {
    return {url: "/user/id/" + id, method: "GET", data: null};
}

const deleteUserParams = id => {
    return {url: "/user/id/" + id, method: "DELETE", data: null};
}

const editUserParams = data => {
    return {url: "/user/edit", method: "POST", data: data};
}

const addUserParams = data => {
    return {url: "/user/add", method: "POST", data: data};
}

const changeUserPasswordParams = data => {
    return {url: "/user/change-password", method: "POST", data: data};
}

export {getUserInfoParams, changeUserPasswordParams, getAllUsersParams, getUserParams, editUserParams, addUserParams, deleteUserParams};

