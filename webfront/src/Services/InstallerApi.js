const getInitializeUserParams = data => {
    return { url: "/api/install", method: "POST", data: data };
}

const getIsInstallerNeededParams = () => {
    return { url: "/api/install", method: "GET", data: null };
}

export {
    getInitializeUserParams,
    getIsInstallerNeededParams
}