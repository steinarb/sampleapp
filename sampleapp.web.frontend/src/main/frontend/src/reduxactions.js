import { createAction } from '@reduxjs/toolkit';

export const INCREMENT_STEP_FIELD_MODIFIED = createAction('INCREMENT_STEP_FIELD_MODIFIED');
export const COUNTER_INCREMENT_STEP_REQUEST = createAction('COUNTER_INCREMENT_STEP_REQUEST');
export const COUNTER_INCREMENT_STEP_RECEIVE = createAction('COUNTER_INCREMENT_STEP_RECEIVE');
export const COUNTER_INCREMENT_STEP_FAILURE = createAction('COUNTER_INCREMENT_STEP_FAILURE');
export const UPDATE_COUNTER_INCREMENT_STEP_REQUEST = createAction('UPDATE_COUNTER_INCREMENT_STEP_REQUEST');
export const UPDATE_COUNTER_INCREMENT_STEP_RECEIVE = createAction('UPDATE_COUNTER_INCREMENT_STEP_RECEIVE');
export const UPDATE_COUNTER_INCREMENT_STEP_FAILURE = createAction('UPDATE_COUNTER_INCREMENT_STEP_FAILURE');

export const SELECT_LOCALE = createAction('SELECT_LOCALE');

export const UNAUTHORIZED_401 = createAction('UNAUTHORIZED_401');
export const FORBIDDEN_403 = createAction('FORBIDDEN_403');

export const SET_BASENAME = createAction('SET_BASENAME');
