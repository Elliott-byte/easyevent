import './App.css';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import Auth from './pages/Auth';
import Events from './pages/Events';
import Bookings from './pages/Bookings';
import MainNavigation from "./components/Navigation/MainNavigation";
import {useState} from "react";
import {AuthContext} from "./context/AuthContext";


function App() {
    const [auth, setAuth] = useState("");
    return (
        <AuthContext.Provider value={{auth, setAuth}}>
            <BrowserRouter>
                <MainNavigation/>
                <main className='main-content'/>
                <Routes>
                  {!auth &&  <Route path='/*' element={<Auth/>}/>}
                  {auth &&  <Route path='/' element={<Navigate to={"/events"}/>}/>}
                  {auth &&  <Route path='/auth' element={<Navigate to={"/events"}/>}/>}
                  <Route path='/events' element={<Events/>}/>
                  {auth &&  <Route path='/bookings' element={<Bookings/>}/>}

                  {/*<Route path='/auth' element={<Auth/>}/>*/}
                  {/*  <Route path='/events' element={<Events/>}/>*/}
                  {/*  <Route path='/bookings' element={<Bookings/>}/>*/}
                </Routes>
            </BrowserRouter>
        </AuthContext.Provider>
    );
}

export default App;
