import 'regenerator-runtime/runtime';
import { fork, all } from "redux-saga/effects";
import loginSaga from './loginSaga';
import logoutSaga from './logoutSaga';
import loginstateSaga from './loginstateSaga';
import localeSaga from './localeSaga';
import defaultLocaleSaga from './defaultLocaleSaga';
import availableLocalesSaga from './availableLocalesSaga';
import displayTextsSaga from './displayTextsSaga';
import locationSaga from './locationSaga';
import accountsSaga from './accountsSaga';
import counterIncrementStepSaga from './counterIncrementStepSaga';
import counterSaga from './counterSaga';
import reloadOnLogoutSaga from './reloadOnLogoutSaga';

export default function* rootSaga() {
    yield all([
        fork(loginSaga),
        fork(logoutSaga),
        fork(loginstateSaga),
        fork(localeSaga),
        fork(defaultLocaleSaga),
        fork(availableLocalesSaga),
        fork(displayTextsSaga),
        fork(locationSaga),
        fork(accountsSaga),
        fork(counterIncrementStepSaga),
        fork(counterSaga),
        fork(reloadOnLogoutSaga),
    ]);
}
