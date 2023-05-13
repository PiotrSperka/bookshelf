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

const DoRefresh = token => {
    return new Promise( ( resolve, reject ) => {
        fetch( '/api/auth/refresh', {
            method: 'GET',
            headers: { "Content-Type": "application/json", "Authorization": "Bearer " + token },
            body: null
        } ).then( response => {
            if ( response.status === 200 ) {
                response.json().then( json => {
                    if ( json.token ) {
                        resolve( json.token );
                    } else {
                        reject( null );
                    }
                } ).catch( err => {
                    reject( err );
                } )
            } else {
                reject( null );
            }
        } ).catch( err => {
            reject( err );
        } )
    } )
}

export { DoLogin, DoLogout, DoRefresh };
