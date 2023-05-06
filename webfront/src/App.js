import styles from './App.module.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import BookGrid from "./Components/BookGrid";
import Login from "./Components/Login"
import TopBar from "./Components/TopBar";
import UserAdmin from "./Components/UserAdmin";
import SystemAdmin from "./Components/SystemAdmin";
import BottomBar from "./Components/BottomBar";
import { useIntl } from "react-intl";
import React, { useEffect } from "react";


const App = () => {
    const intl = useIntl();

    useEffect( () => {
        document.title = intl.formatMessage( { id: "app.header.name" } );
    }, [] )

    return (
        <div className={ styles.appMainDiv }>
            <BrowserRouter>
                <Login/>
                <TopBar/>
                <Routes>
                    <Route path="/">
                        <Route index={ true } element={ <BookGrid/> }/>
                        <Route path="user" element={ <UserAdmin/> }/>
                        <Route path="admin" element={ <SystemAdmin/> }/>
                    </Route>
                    <Route path="*" element={ <h2>404 :(</h2> }/>
                </Routes>
                <div className={ styles.appMainSpacer }></div>
                <BottomBar/>
            </BrowserRouter>
        </div>
    );
}

export default App;
