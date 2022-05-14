import { createReducer } from '@reduxjs/toolkit';
import {
    DISPLAY_TEXTS_RECEIVE,
} from '../reduxactions';

const displayTextsReducer = createReducer([], {
    [DISPLAY_TEXTS_RECEIVE]: (state, action) => action.payload,
});

export default displayTextsReducer;
