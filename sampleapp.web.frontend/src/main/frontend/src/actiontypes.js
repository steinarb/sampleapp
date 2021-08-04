import { createAction } from '@reduxjs/toolkit';

export const USERNAME_MODIFY = createAction('USERNAME_MODIFY');
export const PASSWORD_MODIFY = createAction('PASSWORD_MODIFY');

export const LOGIN_REQUEST = createAction('LOGIN_REQUEST');
export const LOGIN_RECEIVE = createAction('LOGIN_RECEIVE');
export const LOGIN_ERROR = createAction('LOGIN_ERROR');

export const LOGOUT_REQUEST = createAction('LOGOUT_REQUEST');
export const LOGOUT_RECEIVE = createAction('LOGOUT_RECEIVE');
export const LOGOUT_ERROR = createAction('LOGOUT_ERROR');

export const LOGINSTATE_REQUEST = createAction('LOGINSTATE_REQUEST');
export const LOGINSTATE_RECEIVE = createAction('LOGINSTATE_RECEIVE');
export const LOGINSTATE_ERROR = createAction('LOGINSTATE_ERROR');

export const ACCOUNTS_REQUEST = createAction('ACCOUNTS_REQUEST');
export const ACCOUNTS_RECEIVE = createAction('ACCOUNTS_RECEIVE');
export const ACCOUNTS_ERROR = createAction('ACCOUNTS_ERROR');

export const DEFAULT_LOCALE_REQUEST = createAction('DEFAULT_LOCALE_REQUEST');
export const DEFAULT_LOCALE_RECEIVE = createAction('DEFAULT_LOCALE_RECEIVE');
export const DEFAULT_LOCALE_ERROR = createAction('DEFAULT_LOCALE_ERROR');
export const UPDATE_LOCALE = createAction('UPDATE_LOCALE');
export const AVAILABLE_LOCALES_REQUEST = createAction('AVAILABLE_LOCALES_REQUEST');
export const AVAILABLE_LOCALES_RECEIVE = createAction('AVAILABLE_LOCALES_RECEIVE');
export const AVAILABLE_LOCALES_ERROR = createAction('AVAILABLE_LOCALES_ERROR');
export const DISPLAY_TEXTS_REQUEST = createAction('DISPLAY_TEXTS_REQUEST');
export const DISPLAY_TEXTS_RECEIVE = createAction('DISPLAY_TEXTS_RECEIVE');
export const DISPLAY_TEXTS_ERROR = createAction('DISPLAY_TEXTS_ERROR');
