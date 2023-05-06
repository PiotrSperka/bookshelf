import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { UserContextProvider } from "./UserContextProvider";
import { IntlProvider } from "react-intl";
import messages_en from "./Translations/en.json"
import messages_pl from "./Translations/pl.json"

const messages = {
    'en': messages_en,
    'pl': messages_pl
};

const locale = navigator.language;
const language = locale.split( /[-_]/ )[ 0 ] in messages ? locale.split( /[-_]/ )[ 0 ] : 'en';

const root = ReactDOM.createRoot( document.getElementById( 'root' ) );
root.render(
    <React.StrictMode>
        <IntlProvider locale={ language } defaultLocale={ 'en' } messages={ messages[ language ] }>
            <UserContextProvider>
                <App/>
            </UserContextProvider>
        </IntlProvider>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
