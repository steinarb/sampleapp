import { takeLatest, call, put, select } from 'redux-saga/effects';
import axios from 'axios';
import {
    LOGINSTATE_REQUEST,
    LOGINSTATE_RECEIVE,
    LOGINSTATE_FAILURE,
} from '../reduxactions';

export default function* loginstateSaga() {
    yield takeLatest(LOGINSTATE_REQUEST, mottaLoginstateResult);
}

function* mottaLoginstateResult() {
    try {
        const locale = yield select(state => state.locale);
        const response = yield call(sendLoginstate, locale);
        const logoutresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(LOGINSTATE_RECEIVE(logoutresult));
    } catch (error) {
        yield put(LOGINSTATE_FAILURE(error));
    }
}

function sendLoginstate(locale) {
    return axios.get('/api/loginstate', { params: { locale } });
}
