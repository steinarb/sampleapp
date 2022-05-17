import { takeLatest, put } from 'redux-saga/effects';
import Cookies from 'js-cookie';
import {
    SELECT_LOCALE,
    DEFAULT_LOCALE_RECEIVE,
} from '../reduxactions';

export default function* localeSaga() {
    yield takeLatest(DEFAULT_LOCALE_RECEIVE, setLocaleCookieIfNotPresentAndPutCookieValueAsLocale);
    yield takeLatest(SELECT_LOCALE, updateLocaleCookie);
}

export function* setLocaleCookieIfNotPresentAndPutCookieValueAsLocale(action) {
    const currentLocale = Cookies.get('locale');
    if (!currentLocale) {
        Cookies.set('locale', action.payload);
    }
    yield put(SELECT_LOCALE(Cookies.get('locale')));

}

function* updateLocaleCookie(action) {
    yield Cookies.set('locale', action.payload);
}
