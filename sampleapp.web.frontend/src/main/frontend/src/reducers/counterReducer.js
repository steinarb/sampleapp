import { createReducer } from '@reduxjs/toolkit';
import {
    COUNTER_RECEIVE,
    COUNTER_DECREMENT_RECEIVE,
    COUNTER_INCREMENT_RECEIVE,
} from '../reduxactions';

const counterReducer = createReducer(1, builder => {
    builder
        .addCase(COUNTER_RECEIVE, (state, action) => action.payload.counter)
        .addCase(COUNTER_DECREMENT_RECEIVE, (state, action) => action.payload.counter)
        .addCase(COUNTER_INCREMENT_RECEIVE, (state, action) => action.payload.counter);
});

export default counterReducer;
