import { combineReducers } from 'redux';
import { createReducer } from '@reduxjs/toolkit';
import { api } from '../api';
import counterIncrementStep from './counterIncrementStepReducer';
import username from './usernameReducer';
import locale from './localeReducer';

export default (basename) => combineReducers({
    [api.reducerPath]: api.reducer,
    counterIncrementStep,
    username,
    locale,
    basename: createReducer(basename, (builder) => builder),
});
