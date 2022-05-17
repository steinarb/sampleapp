import { createReducer } from '@reduxjs/toolkit';
import Cookies from 'js-cookie';
import {
    SELECT_LOCALE,
} from '../reduxactions';

const currentLocale = Cookies.get('locale') || '';

const localeReducer = createReducer(currentLocale, {
    [SELECT_LOCALE]: (state, action) => action.payload,
});

export default localeReducer;
