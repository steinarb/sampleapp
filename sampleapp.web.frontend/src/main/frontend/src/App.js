import React, { Component } from 'react';
import { Routes, Route } from 'react-router';
import { HistoryRouter as Router } from "redux-first-history/rr6";
import './App.css';
import Home from './components/Home';
import Counter from './components/Counter';
import Login from './components/Login';
import Unauthorized from './components/Unauthorized';

export default function App(props) {
    const { history, basename } = props;

    return (
        <Router history={history} basename={basename}>
            <Routes>
                <Route exact path="/" element={<Home/>} />
                <Route exact path="/counter" element={<Counter/>} />
                <Route exact path="/login" element={<Login/>} />
                <Route exact path="/unauthorized" element={<Unauthorized/>} />
            </Routes>
        </Router>
    );
}
