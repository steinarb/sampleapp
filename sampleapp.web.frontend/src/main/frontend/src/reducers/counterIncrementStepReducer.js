import { createReducer } from '@reduxjs/toolkit';
import {
    INCREMENT_STEP_FIELD_MODIFIED,
    COUNTER_INCREMENT_STEP_RECEIVE,
    UPDATE_COUNTER_INCREMENT_STEP_RECEIVE,
 } from '../reduxactions';

const counterIncrementStepReducer = createReducer(1, builder => {
    builder
        .addCase(INCREMENT_STEP_FIELD_MODIFIED, (state, action) => parseInt(action.payload) || 0)
        .addCase(COUNTER_INCREMENT_STEP_RECEIVE, (state, action) => action.payload.counterIncrementStep)
        .addCase(UPDATE_COUNTER_INCREMENT_STEP_RECEIVE, (state, action) => action.payload.counterIncrementStep);
});

export default counterIncrementStepReducer;
