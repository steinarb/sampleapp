import { createReducer } from '@reduxjs/toolkit';
import {
    LOGIN_FAILURE,
} from '../reduxactions';

const errorsReducer = createReducer({}, {
    [LOGIN_FAILURE]: (state, action) => {
        const login = action.payload;
        return { ...state, login };
    },
});

export default errorsReducer;
