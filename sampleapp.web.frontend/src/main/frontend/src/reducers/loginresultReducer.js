import { createReducer } from '@reduxjs/toolkit';
import {
    LOGIN_RECEIVE,
    LOGOUT_RECEIVE,
    LOGINSTATE_RECEIVE,
} from '../reduxactions';

const loginresultReducer = createReducer({ authorized: true, user: {} }, builder => {
    builder
        .addCase(LOGIN_RECEIVE, (state, action) => action.payload)
        .addCase(LOGOUT_RECEIVE, (state, action) => action.payload)
        .addCase(LOGINSTATE_RECEIVE, (state, action) => action.payload);
});

export default loginresultReducer;
