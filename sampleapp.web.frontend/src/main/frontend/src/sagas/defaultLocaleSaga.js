import { takeLatest, call, put } from 'redux-saga/effects';
import axios from 'axios';
import {
    DEFAULT_LOCALE_REQUEST,
    DEFAULT_LOCALE_RECEIVE,
    DEFAULT_LOCALE_FAILURE,
} from '../reduxactions';

// watcher saga
export default function* defaultLocaleSaga() {
    yield takeLatest(DEFAULT_LOCALE_REQUEST, receiveDefaultLocaleSaga);
}

// worker saga
function* receiveDefaultLocaleSaga() {
    try {
        const response = yield call(doDefaultLocale);
        const defaultLocale = (response.headers['content-type'] == 'application/json') ? response.data : '';
        yield put(DEFAULT_LOCALE_RECEIVE(defaultLocale));
    } catch (error) {
        yield put(DEFAULT_LOCALE_FAILURE(error));
    }
}

function doDefaultLocale() {
    return axios.get('/api/defaultlocale');
}
