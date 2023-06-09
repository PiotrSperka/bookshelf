import { useState } from "react";
import { useUserContext } from "../UserContextProvider";

const getHeaders = ( userContext, explicitToken ) => {
    const token = ( explicitToken != null ) ? explicitToken : ( userContext != null ) ? userContext.token : null;
    if ( token ) {
        return { "Content-Type": "application/json", "Authorization": "Bearer " + token };
    } else {
        return { "Content-Type": "application/json" };
    }
}

const useApi = () => {
    const { user, logout, isLoggedIn } = useUserContext();
    const [ data, setData ] = useState( null );
    const [ error, setError ] = useState( "" );
    const [ loading, setLoading ] = useState( false );

    const request = async ( parameters ) => {
        return new Promise( ( resolve, reject ) => {
            setLoading( true );
            setError( "" );
            fetch( parameters.url, {
                method: parameters.method,
                headers: getHeaders( user, parameters.token ),
                body: ( parameters.data ) ? JSON.stringify( parameters.data ) : null
            } ).then( result => {
                if ( result.status === 401 ) {
                    if ( isLoggedIn() ) {
                        logout() // clear token, as it is invalid now
                        reject( "Unauthorized" );
                    }
                } else if ( result.ok ) {
                    setData( result );
                    resolve( result );
                } else {
                    setData( result );
                    setError( result.statusText || "Unexpected Error!" );
                    reject( result.statusText || "Unexpected Error!" );
                }
            } ).catch( err => {
                setError( err.message || "Unexpected Error!" );
                reject( err.statusText || "Unexpected Error!" );
            } ).finally( () => {
                setLoading( false );
            } );
        } );
    };

    const reset = () => {
        setData( null );
        setError( "" );
        setLoading( false );
    }

    return {
        data,
        error,
        loading,
        request,
        reset
    };
};

export { useApi };
