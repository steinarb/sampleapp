import { createReducer } from '@reduxjs/toolkit';
import { INCREMENT_STEP_FIELD_MODIFIED } from '../reduxactions';
import { api } from '../api';

const counterIncrementStepReducer = createReducer(1, builder => {
    builder
        .addCase(INCREMENT_STEP_FIELD_MODIFIED, (state, action) => parseInt(action.payload) || 0)
        .addMatcher(api.endpoints.getCounterIncrementStep.matchFulfilled, (state, action) => action.payload.counterIncrementStep);
});

export default counterIncrementStepReducer;
