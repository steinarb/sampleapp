import { createReducer } from '@reduxjs/toolkit';
import {
    PASSWORD_MODIFY,
    LOGIN_RECEIVE,
} from '../actiontypes';

const passwordReducer = createReducer('', {
    [PASSWORD_MODIFY]: (state, action) => action.payload,
    [LOGIN_RECEIVE]: (state, action) => action.payload.success ? '' : state,
});

export default passwordReducer;
