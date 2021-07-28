import 'regenerator-runtime/runtime';
import { fork, all } from "redux-saga/effects";
import loginSaga from './loginSaga';
import logoutSaga from './logoutSaga';
import loginstateSaga from './loginstateSaga';
import locationSaga from './locationSaga';
import accountsSaga from './accountsSaga';

export default function* rootSaga() {
    yield all([
        fork(loginSaga),
        fork(logoutSaga),
        fork(loginstateSaga),
        fork(locationSaga),
        fork(accountsSaga),
    ]);
}
