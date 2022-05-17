import { createReducer } from '@reduxjs/toolkit';
import {
    LOGIN_FAILURE,
} from '../reduxactions';

const errorsReducer = createReducer({}, {
    [LOGIN_FAILURE]: (state, action) => ({ ...state, login: action.payload }),
});

export default errorsReducer;
