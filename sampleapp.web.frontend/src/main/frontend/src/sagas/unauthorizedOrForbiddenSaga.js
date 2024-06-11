import { takeLatest, put } from 'redux-saga/effects';
import {
    UNAUTHORIZED_401,
    FORBIDDEN_403,
    COUNTER_INCREMENT_STEP_FAILURE,
    UPDATE_COUNTER_INCREMENT_STEP_FAILURE,
    COUNTER_FAILURE,
    COUNTER_DECREMENT_FAILURE,
    COUNTER_INCREMENT_FAILURE,
} from '../reduxactions';

export default function* unauthorizedOrForbiddenSaga() {
    yield takeLatest(COUNTER_INCREMENT_STEP_FAILURE, convertStatusToAction);
    yield takeLatest(UPDATE_COUNTER_INCREMENT_STEP_FAILURE, convertStatusToAction);
    yield takeLatest(COUNTER_FAILURE, convertStatusToAction);
    yield takeLatest(COUNTER_DECREMENT_FAILURE, convertStatusToAction);
    yield takeLatest(COUNTER_INCREMENT_FAILURE, convertStatusToAction);
}

function* convertStatusToAction(action) {
    const status = parseInt(action.payload.response.status);
    const url = action.payload.config.url;
    if (status === 401) {
        yield put(UNAUTHORIZED_401(url));
    } else if (status === 403) {
        yield put(FORBIDDEN_403(url));
    }
}
