import { takeLatest, call, put, select } from 'redux-saga/effects';
import axios from 'axios';
import {
    LOGIN_REQUEST,
    LOGIN_RECEIVE,
    LOGIN_FAILURE,
} from '../reduxactions';

export default function* loginSaga() {
    yield takeLatest(LOGIN_REQUEST, mottaLoginResult);
}

function* mottaLoginResult(action) {
    try {
        const locale = yield select(state => state.locale);
        const response = yield call(sendLogin, action.payload, locale);
        const loginresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(LOGIN_RECEIVE(loginresult));
    } catch (error) {
        yield put(LOGIN_FAILURE(error));
    }
}

function sendLogin(credentials, locale) {
    return axios.post('/api/login', credentials, { params: { locale } });
}
