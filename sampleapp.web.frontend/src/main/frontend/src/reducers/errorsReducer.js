import { createReducer } from '@reduxjs/toolkit';
import {
    LOGIN_FAILURE,
    LOGOUT_FAILURE,
    ACCOUNTS_FAILURE,
    COUNTER_INCREMENT_STEP_FAILURE,
    UPDATE_COUNTER_INCREMENT_STEP_FAILURE,
    COUNTER_FAILURE,
    COUNTER_DECREMENT_FAILURE,
    COUNTER_INCREMENT_FAILURE,
    DEFAULT_LOCALE_FAILURE,
    AVAILABLE_LOCALES_FAILURE,
    DISPLAY_TEXTS_FAILURE,
} from '../reduxactions';

const errorsReducer = createReducer({}, builder => {
    builder
        .addCase(LOGIN_FAILURE, (state, action) => ({ ...state, login: action.payload }))
        .addCase(LOGOUT_FAILURE, (state, action) => ({ ...state, logout: action.payload }))
        .addCase(ACCOUNTS_FAILURE, (state, action) => ({ ...state, accounts: action.payload }))
        .addCase(COUNTER_INCREMENT_STEP_FAILURE, (state, action) => ({ ...state, fetchIncrementStep: action.payload }))
        .addCase(UPDATE_COUNTER_INCREMENT_STEP_FAILURE, (state, action) => ({ ...state, saveModifiedIncrementStep: action.payload }))
        .addCase(COUNTER_FAILURE, (state, action) => ({ ...state, fetchCounter: action.payload }))
        .addCase(COUNTER_DECREMENT_FAILURE, (state, action) => ({ ...state, counterDecrement: action.payload }))
        .addCase(COUNTER_INCREMENT_FAILURE, (state, action) => ({ ...state, counterIncrement: action.payload }))
        .addCase(DEFAULT_LOCALE_FAILURE, (state, action) => ({ ...state, fetchDefaultLocale: action.payload }))
        .addCase(AVAILABLE_LOCALES_FAILURE, (state, action) => ({ ...state, fetchAvailableLocales: action.payload }))
        .addCase(DISPLAY_TEXTS_FAILURE, (state, action) => ({ ...state, displayTexts: action.payload }));
});

export default errorsReducer;
