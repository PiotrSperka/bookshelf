import { createContext, useContext, useEffect, useState } from "react";
import { DoLogout, DoRefresh } from "./Services/LoginService";

export const UserContext = createContext();

export const UserContextProvider = ( { children } ) => {
    const [ user, setUser ] = useState( JSON.parse( localStorage.getItem( "userInfo" ) ) );

    useEffect( () => {
        const timer = setInterval( () => {
            refresh();
        }, 60000 );
        return () => clearInterval( timer );
    } )

    const refresh = () => {
        if ( isLoggedIn() ) {
            DoRefresh( user.token ).then( newToken => {
                if ( newToken !== user.token ) {
                    setUser( prevState => ( { ...prevState, token: newToken } ) )
                }
            } ).catch( () => {
                logout()
            } )
        }
    }

    const logout = () => {
        if ( isLoggedIn() ) {
            DoLogout( user.token ).then( () => {
                localStorage.removeItem( "userInfo" );
                setUser( { token: null, name: null, roles: [] } );
            } )
        }
    }

    const isLoggedIn = () => {
        return user != null && !!user.token && !!user.name;
    }

    const setUserInfo = userInfo => {
        localStorage.setItem( "userInfo", JSON.stringify( userInfo ) );
        setUser( userInfo );
    }

    const hasRole = role => {
        return user.roles.indexOf( role ) > -1;
    }

    return (
        <UserContext.Provider value={ { user, logout, isLoggedIn, setUserInfo, hasRole } }>
            { children }
        </UserContext.Provider>
    )
}

export const useUserContext = () => useContext( UserContext );
