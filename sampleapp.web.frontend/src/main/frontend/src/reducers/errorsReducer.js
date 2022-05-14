import { createReducer } from '@reduxjs/toolkit';
import {
    LOGIN_ERROR,
} from '../reduxactions';

const errorsReducer = createReducer({}, {
    [LOGIN_ERROR]: (state, action) => {
        const login = action.payload;
        return { ...state, login };
    },
});

export default errorsReducer;
