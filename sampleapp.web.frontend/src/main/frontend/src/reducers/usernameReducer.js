import { createReducer } from '@reduxjs/toolkit';
import { api } from '../api';

const usernameReducer = createReducer('', builder => {
    builder
        .addMatcher(api.endpoints.getLoginstate.matchFulfilled, (_, action) => action.payload.user.username);
});

export default usernameReducer;
