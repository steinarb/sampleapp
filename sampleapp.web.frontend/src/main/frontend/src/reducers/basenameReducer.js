import { createReducer } from '@reduxjs/toolkit';
import { SET_BASENAME } from '../reduxactions';

const basenameReducer = createReducer('', builder => {
    builder
        .addCase(SET_BASENAME, (state, action) => action.payload);
});

export default basenameReducer;
