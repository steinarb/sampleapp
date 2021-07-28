import { takeLatest, call, put } from 'redux-saga/effects';
import axios from 'axios';
import {
    LOGOUT_REQUEST,
    LOGOUT_RECEIVE,
    LOGOUT_ERROR,
} from '../actiontypes';

function sendLogout() {
    return axios.get('/sampleapp/api/logout');
}

function* mottaLogoutResultat() {
    try {
        const response = yield call(sendLogout);
        const logoutresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(LOGOUT_RECEIVE(logoutresult));
    } catch (error) {
        yield put(LOGOUT_ERROR(error));
    }
}

export default function* logoutSaga() {
    yield takeLatest(LOGOUT_REQUEST, mottaLogoutResultat);
}
