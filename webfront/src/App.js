import './App.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import BookGrid from "./Components/BookGrid";
import Login from "./Components/Login"
import TopBar from "./Components/TopBar";


const App = () => {
    return (
        <div>
            <Login />
            <TopBar />
            <BrowserRouter>
                <Routes>
                    <Route path="/">
                        <Route index={true} element={<BookGrid/>}/>
                        {/*<Route path="login" element={<Login/>}/>*/}
                    </Route>
                    <Route path="*" element={<h2>404 :(</h2>}/>
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;
