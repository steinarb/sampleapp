import { createReducer } from '@reduxjs/toolkit';
import {
    PASSWORD_MODIFY,
    LOGIN_RECEIVE,
} from '../actiontypes';

const passwordReducer = createReducer('', {
    [PASSWORD_MODIFY]: (state, action) => action.payload,
    [LOGIN_RECEIVE]: (state, action) => action.payload.suksess ? '' : state,
});

export default passwordReducer;
