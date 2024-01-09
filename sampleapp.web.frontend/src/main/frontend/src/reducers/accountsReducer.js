import { createReducer } from '@reduxjs/toolkit';
import { ACCOUNTS_RECEIVE } from '../reduxactions';

const accountsReducer = createReducer([], builder => {
    builder
        .addCase(ACCOUNTS_RECEIVE, (state, action) => action.payload);
});

export default accountsReducer;
