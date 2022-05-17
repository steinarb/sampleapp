import { takeLatest, call, put } from 'redux-saga/effects';
import axios from 'axios';
import {
    DISPLAY_TEXTS_REQUEST,
    DISPLAY_TEXTS_RECEIVE,
    DISPLAY_TEXTS_FAILURE,
    SELECT_LOCALE,
} from '../reduxactions';

// watcher saga
export default function* displayTextsSaga() {
    yield takeLatest(DISPLAY_TEXTS_REQUEST, receiveDisplayTextsSaga);
    yield takeLatest(SELECT_LOCALE, receiveDisplayTextsSaga);
}

function doDisplayTexts(locale) {
    return axios.get('/api/displaytexts', { params: { locale } });
}

// worker saga
function* receiveDisplayTextsSaga(action) {
    try {
        const response = yield call(doDisplayTexts, action.payload);
        const displayTexts = (response.headers['content-type'] == 'application/json') ? response.data : {};
        yield put(DISPLAY_TEXTS_RECEIVE(displayTexts));
    } catch (error) {
        yield put(DISPLAY_TEXTS_FAILURE(error));
    }
}
