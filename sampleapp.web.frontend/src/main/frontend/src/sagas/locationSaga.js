import { takeLatest, put } from 'redux-saga/effects';
import { LOCATION_CHANGE } from 'connected-react-router';
import { ACCOUNTS_REQUEST } from '../reduxactions';

function* locationChange(action) {
    const { location = {} } = action.payload || {};
    const { pathname = '' } = location;

    if (pathname === '/') {
        yield put(ACCOUNTS_REQUEST());
    }
}

export default function* locationSaga() {
    yield takeLatest(LOCATION_CHANGE, locationChange);
}
