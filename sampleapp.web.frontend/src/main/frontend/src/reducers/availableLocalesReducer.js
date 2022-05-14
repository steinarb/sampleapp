import { createReducer } from '@reduxjs/toolkit';
import {
    AVAILABLE_LOCALES_RECEIVE,
} from '../reduxactions';

const availableLocalesReducer = createReducer([], {
    [AVAILABLE_LOCALES_RECEIVE]: (state, action) => action.payload,
});

export default availableLocalesReducer;
