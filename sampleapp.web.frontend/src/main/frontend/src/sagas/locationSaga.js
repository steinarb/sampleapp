import { takeLatest, put, select } from 'redux-saga/effects';
import { LOCATION_CHANGE } from 'redux-first-history';
import { ACCOUNTS_REQUEST } from '../reduxactions';

export default function* locationSaga() {
    yield takeLatest(LOCATION_CHANGE, locationChange);
}

function* locationChange(action) {
    const { location = {} } = action.payload || {};
    const basename = yield select(state => state.router.basename);
    const pathname = findPathname(location, basename);

    if (pathname === '/') {
        yield put(ACCOUNTS_REQUEST());
    }
}

function findPathname(location, basename) {
    if (basename === '/') {
        return location.pathname;
    }

    return location.pathname.replace(new RegExp('^' + basename + '(.*)'), '$1');
}
