import { takeLatest, call, put } from 'redux-saga/effects';
import axios from 'axios';
import {
    ACCOUNTS_REQUEST,
    ACCOUNTS_RECEIVE,
    ACCOUNTS_ERROR,
} from '../actiontypes';

function sendAccounts() {
    return axios.get('/sampleapp/api/accounts');
}

function* mottaAccountsResultat() {
    try {
        const response = yield call(sendAccounts);
        const accountsresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(ACCOUNTS_RECEIVE(accountsresult));
    } catch (error) {
        yield put(ACCOUNTS_ERROR(error));
    }
}

export default function* accountsSaga() {
    yield takeLatest(ACCOUNTS_REQUEST, mottaAccountsResultat);
}
