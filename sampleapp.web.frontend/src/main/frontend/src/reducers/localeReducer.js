import { createReducer } from '@reduxjs/toolkit';
import Cookies from 'js-cookie';
import {
    UPDATE_LOCALE,
} from '../actiontypes';

const currentLocale = Cookies.get('locale') || '';

const localeReducer = createReducer(currentLocale, {
    [UPDATE_LOCALE]: (state, action) => action.payload,
});

export default localeReducer;
