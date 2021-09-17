import { createReducer } from '@reduxjs/toolkit';
import {
    COUNTER_INCREMENT_STEP_MODIFY,
    COUNTER_INCREMENT_STEP_RECEIVE,
    UPDATE_COUNTER_INCREMENT_STEP_RECEIVE,
 } from '../actiontypes';

const counterIncrementStepReducer = createReducer(1, {
    [COUNTER_INCREMENT_STEP_MODIFY]: (state, action) => parseInt(action.payload) || 0,
    [COUNTER_INCREMENT_STEP_RECEIVE]: (state, action) => action.payload.counterIncrementStep,
    [UPDATE_COUNTER_INCREMENT_STEP_RECEIVE]: (state, action) => action.payload.counterIncrementStep,
});

export default counterIncrementStepReducer;
