import styles from './App.module.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./Components/Login"
import TopBar from "./Components/TopBar/TopBar";
import UserAdmin from "./Components/UserAdmin";
import SystemAdmin from "./Components/Admin/SystemAdmin";
import BottomBar from "./Components/BottomBar";
import { useIntl } from "react-intl";
import React, { useEffect } from "react";
import { useUserContext } from "./UserContextProvider";
import BooksDatabaseViewer from "./Components/BooksDataGrid/BooksDatabaseViewer";


const App = () => {
    const intl = useIntl();
    const { isLoggedIn } = useUserContext();

    useEffect( () => {
        document.title = intl.formatMessage( { id: "app.header.name" } );
    }, [] )

    return (
        <div className={ styles.appMainDiv }>
            <BrowserRouter>
                <Login/>
                { isLoggedIn() && <TopBar/> }
                { isLoggedIn() &&
                    <Routes>
                        <Route path="/">
                            <Route index={ true } element={ <BooksDatabaseViewer/> }/>
                            <Route path="user" element={ <UserAdmin/> }/>
                            <Route path="admin" element={ <SystemAdmin/> }/>
                        </Route>
                        <Route path="*" element={ <h2>404 :(</h2> }/>
                    </Routes> }
                <div className={ styles.appMainSpacer }></div>
                <BottomBar/>
            </BrowserRouter>
        </div>
    );
}

export default App;
