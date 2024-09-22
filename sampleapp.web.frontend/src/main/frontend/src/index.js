import React from 'react';
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import { configureStore, Tuple } from '@reduxjs/toolkit';
import createSagaMiddleware from 'redux-saga';
import { Provider } from 'react-redux';
import { createReduxHistoryContext } from "redux-first-history";
import { createBrowserHistory } from 'history';
import createRootReducer from './reducers';
import rootSaga from './sagas';
import {
    LOGINSTATE_REQUEST,
    DEFAULT_LOCALE_REQUEST,
    AVAILABLE_LOCALES_REQUEST,
    SET_BASENAME,
} from './reduxactions';

const baseUrl = Array.from(document.scripts).map(s => s.src).filter(src => src.includes('assets/'))[0].replace(/\/assets\/.*/, '');
const basename = new URL(baseUrl).pathname;
axios.defaults.baseURL = baseUrl;
const sagaMiddleware = createSagaMiddleware();
const {
  createReduxHistory,
  routerMiddleware,
  routerReducer
} = createReduxHistoryContext({ history: createBrowserHistory(), basename });
const store = configureStore({
    reducer: createRootReducer(routerReducer),
    middleware: () => new Tuple(sagaMiddleware, routerMiddleware),
});
store.dispatch(SET_BASENAME(basename));
sagaMiddleware.run(rootSaga);
const history = createReduxHistory(store);

store.dispatch(LOGINSTATE_REQUEST());
store.dispatch(DEFAULT_LOCALE_REQUEST());
store.dispatch(AVAILABLE_LOCALES_REQUEST());

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
    <Provider store={store}>
        <App history={history} basename={basename} />
    </Provider>,
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
