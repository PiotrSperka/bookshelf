import {createContext, useContext, useState} from "react";

export const UserContext = createContext();

export const UserContextProvider = ({children}) => {
    const [user, setUser] = useState({token: localStorage.getItem("token")});

    const logout = () => {
        localStorage.removeItem("token");
        setUser({token: null});
    }

    const isLoggedIn = () => {
        return !!user.token;
    }

    const setUserToken = token => {
        localStorage.setItem("token", token);
        setUser({token: token});
    }

    return (
        <UserContext.Provider value={{user, logout, isLoggedIn, setUserToken}}>
            {children}
        </UserContext.Provider>
    )
}

export const useUserContext = () => useContext(UserContext);
