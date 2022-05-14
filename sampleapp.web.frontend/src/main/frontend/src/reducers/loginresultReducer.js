import { createReducer } from '@reduxjs/toolkit';
import {
    LOGIN_RECEIVE,
    LOGOUT_RECEIVE,
    LOGINSTATE_RECEIVE,
} from '../reduxactions';

const loginresultReducer = createReducer({ authorized: true, user: {} }, {
    [LOGIN_RECEIVE]: (state, action) => action.payload,
    [LOGOUT_RECEIVE]: (state, action) => action.payload,
    [LOGINSTATE_RECEIVE]: (state, action) => action.payload,
});

export default loginresultReducer;
