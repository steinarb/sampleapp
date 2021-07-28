import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import { configureStore } from '@reduxjs/toolkit';
import createSagaMiddleware from 'redux-saga';
import { Provider } from 'react-redux';
import { routerMiddleware } from 'connected-react-router';
import { createBrowserHistory } from 'history';
import createRootReducer from './reducers';
import rootSaga from './sagas';
import { LOGINSTATE_REQUEST } from './actiontypes';

const sagaMiddleware = createSagaMiddleware();
const history = createBrowserHistory();
const store = configureStore({
    reducer: createRootReducer(history),
    middleware: [
        sagaMiddleware,
        routerMiddleware(history),
    ],
});
sagaMiddleware.run(rootSaga);

store.dispatch(LOGINSTATE_REQUEST());

ReactDOM.render(
    <Provider store={store}>
        <App history={history} />
    </Provider>,
    document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();
