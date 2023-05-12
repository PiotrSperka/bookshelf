const DoLogin = ( form ) => {
    return new Promise( ( resolve, reject ) => {
        fetch( '/api/auth/login', {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify( form )
        } ).then( response => {
            if ( response.status === 200 ) {
                response.json().then( json => {
                    if ( json.token ) {
                        resolve( json.token );
                    } else {
                        resolve( null );
                    }
                } ).catch( err => {
                    reject( err );
                } )
            } else {
                resolve( null );
            }
        } ).catch( err => {
            console.log( err );
            reject( err );
        } )
    } );
}

const DoLogout = token => {
    return fetch( '/api/auth/logout', {
        method: 'GET',
        headers: { "Content-Type": "application/json", "Authorization": "Bearer " + token },
        body: null
    } )
}

export { DoLogin, DoLogout };
