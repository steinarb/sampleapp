import { takeLatest, select, put } from 'redux-saga/effects';
import { LOCATION_CHANGE } from 'connected-react-router';
import { ACCOUNTS_REQUEST } from '../actiontypes';

function* locationChange(action) {
    const { location = {} } = action.payload || {};
    const { pathname = '' } = location;

    if (pathname === '/sampleapp/') {
        yield select(state => state.username);
        yield put(ACCOUNTS_REQUEST());
    }
}

export default function* locationSaga() {
    yield takeLatest(LOCATION_CHANGE, locationChange);
}
