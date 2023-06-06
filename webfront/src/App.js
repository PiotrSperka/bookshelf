import styles from './App.module.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./Components/Auth/Login"
import TopBar from "./Components/TopBar/TopBar";
import UserAdmin from "./Components/UserAdmin";
import SystemAdmin from "./Components/Admin/SystemAdmin";
import BottomBar from "./Components/BottomBar";
import { useIntl } from "react-intl";
import React, { useEffect } from "react";
import { useUserContext } from "./UserContextProvider";
import BooksDatabaseViewer from "./Components/BooksDataGrid/BooksDatabaseViewer";
import ResetPassword from "./Components/Auth/ResetPassword";
import RequestNewPassword from "./Components/Auth/RequestNewPassword";


const App = () => {
    const intl = useIntl();
    const { isLoggedIn } = useUserContext();

    useEffect( () => {
        document.title = intl.formatMessage( { id: "app.header.name" } );
    }, [] )

    const excludeLogin = () => {
        return window.location.pathname.startsWith( "/set-password/" ) === false || window.location.pathname.startsWith( "/lost-password" ) === false;
    }

    return (
        <div className={ styles.appMainDiv }>
            <BrowserRouter>
                { excludeLogin() && <Login/> }
                { isLoggedIn() && <TopBar/> }
                <Routes>
                    <Route path="/set-password/*" element={ <ResetPassword/> }/>
                    <Route path="/lost-password" element={ <RequestNewPassword/> }/>
                    { isLoggedIn() && <Route path="/">
                        <Route index={ true } element={ <BooksDatabaseViewer/> }/>
                        <Route path="user" element={ <UserAdmin/> }/>
                        <Route path="admin" element={ <SystemAdmin/> }/>
                    </Route> }
                    <Route path="*" element={ <h2>404 :(</h2> }/>
                </Routes>
                <div className={ styles.appMainSpacer }></div>
                <BottomBar/>
            </BrowserRouter>
        </div>
    );
}

export default App;
