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

const errorsReducer = createReducer({}, {
    [LOGIN_FAILURE]: (state, action) => ({ ...state, login: action.payload }),
    [LOGOUT_FAILURE]: (state, action) => ({ ...state, logout: action.payload }),
    [ACCOUNTS_FAILURE]: (state, action) => ({ ...state, accounts: action.payload }),
    [COUNTER_INCREMENT_STEP_FAILURE]: (state, action) => ({ ...state, fetchIncrementStep: action.payload }),
    [UPDATE_COUNTER_INCREMENT_STEP_FAILURE]: (state, action) => ({ ...state, saveModifiedIncrementStep: action.payload }),
    [COUNTER_FAILURE]: (state, action) => ({ ...state, fetchCounter: action.payload }),
    [COUNTER_DECREMENT_FAILURE]: (state, action) => ({ ...state, counterDecrement: action.payload }),
    [COUNTER_INCREMENT_FAILURE]: (state, action) => ({ ...state, counterIncrement: action.payload }),
    [DEFAULT_LOCALE_FAILURE]: (state, action) => ({ ...state, fetchDefaultLocale: action.payload }),
    [AVAILABLE_LOCALES_FAILURE]: (state, action) => ({ ...state, fetchAvailableLocales: action.payload }),
    [DISPLAY_TEXTS_FAILURE]: (state, action) => ({ ...state, displayTexts: action.payload }),
});

export default errorsReducer;
