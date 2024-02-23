import { takeLatest } from 'redux-saga/effects';
import { LOGOUT_RECEIVE } from '../reduxactions';

export default function* reloadOnLogoutSaga() {
    yield takeLatest(LOGOUT_RECEIVE, reloadPageInBrowserOnSuccessfulLogout);
}

function reloadPageInBrowserOnSuccessfulLogout() {
    // Reload page in browser to let shiro handle redirect to login page
    location.reload();
}
