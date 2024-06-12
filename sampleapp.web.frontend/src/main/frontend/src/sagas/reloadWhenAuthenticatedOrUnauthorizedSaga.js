import { takeLatest, put } from 'redux-saga/effects';
import { push } from 'redux-first-history';
import {
    UNAUTHORIZED_401,
    FORBIDDEN_403,
} from '../reduxactions';

export default function* navigateToLoginOrUnauthorizedSaga() {
    yield takeLatest(UNAUTHORIZED_401, navigateToLogin);
    yield takeLatest(FORBIDDEN_403, navigateToUnauthorized);
}

function* navigateToLogin() {
    yield put(push('/login'));
}

function* navigateToUnauthorized() {
    yield put(push('/unauthorized'));
}
