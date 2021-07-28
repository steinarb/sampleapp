import { createReducer } from '@reduxjs/toolkit';
import {
    USERNAME_MODIFY,
} from '../actiontypes';

const usernameReducer = createReducer('', {
    [USERNAME_MODIFY]: (state, action) => action.payload,
});

export default usernameReducer;
