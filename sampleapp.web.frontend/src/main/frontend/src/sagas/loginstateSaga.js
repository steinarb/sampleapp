import { takeLatest, call, put } from 'redux-saga/effects';
import axios from 'axios';
import {
    LOGINSTATE_REQUEST,
    LOGINSTATE_RECEIVE,
    LOGINSTATE_ERROR,
} from '../actiontypes';

function sendLoginstate() {
    return axios.get('/sampleapp/api/loginstate');
}

function* mottaLoginstateResultat() {
    try {
        const response = yield call(sendLoginstate);
        const logoutresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(LOGINSTATE_RECEIVE(logoutresult));
    } catch (error) {
        yield put(LOGINSTATE_ERROR(error));
    }
}

export default function* loginstateSaga() {
    yield takeLatest(LOGINSTATE_REQUEST, mottaLoginstateResultat);
}
