import { createContext, useContext, useState } from "react";

export const UserContext = createContext();

export const UserContextProvider = ( { children } ) => {
    const [ user, setUser ] = useState( JSON.parse( localStorage.getItem( "userInfo" ) ) );

    const logout = () => {
        localStorage.removeItem( "userInfo" );
        setUser( { token: null, name: null, roles: [] } );
    }

    const isLoggedIn = () => {
        return user != null && !!user.token && !!user.name;
    }

    const setUserInfo = userInfo => {
        localStorage.setItem( "userInfo", JSON.stringify( userInfo ) );
        setUser( userInfo );
    }

    const hasRole = role => {
        return user.roles.indexOf(role) > -1;
    }

    return (
        <UserContext.Provider value={ { user, logout, isLoggedIn, setUserInfo, hasRole } }>
            { children }
        </UserContext.Provider>
    )
}

export const useUserContext = () => useContext( UserContext );
