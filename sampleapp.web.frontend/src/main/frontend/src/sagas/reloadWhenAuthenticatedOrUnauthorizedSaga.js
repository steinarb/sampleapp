import { takeLatest } from 'redux-saga/effects';
import {
    UNAUTHORIZED_401,
    FORBIDDEN_403,
} from '../reduxactions';

export default function* reloadWhenAuthenticatedOrUnauthorizedSaga() {
    yield takeLatest(UNAUTHORIZED_401, reloadCurrentPage);
    yield takeLatest(FORBIDDEN_403, reloadCurrentPage);
}

function reloadCurrentPage() {
    window.location.reload();
}
