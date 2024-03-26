import { takeLatest, call, put, select, delay } from 'redux-saga/effects';
import axios from 'axios';
import {
    COUNTER_INCREMENT_STEP_REQUEST,
    COUNTER_INCREMENT_STEP_RECEIVE,
    COUNTER_INCREMENT_STEP_FAILURE,
    LOGIN_RECEIVE,
    LOGINSTATE_RECEIVE,
    UPDATE_COUNTER_INCREMENT_STEP_REQUEST,
    UPDATE_COUNTER_INCREMENT_STEP_RECEIVE,
    UPDATE_COUNTER_INCREMENT_STEP_FAILURE,
    INCREMENT_STEP_FIELD_MODIFIED,
} from '../reduxactions';

export default function* counterIncrementStepSaga() {
    yield takeLatest(COUNTER_INCREMENT_STEP_REQUEST, fetchCounterIncrementStep);
    yield takeLatest(LOGIN_RECEIVE, fetchCounterIncrementStep);
    yield takeLatest(LOGINSTATE_RECEIVE, fetchCounterIncrementStep);
    yield takeLatest(UPDATE_COUNTER_INCREMENT_STEP_REQUEST, updateCounterIncrementStep);
    yield takeLatest(INCREMENT_STEP_FIELD_MODIFIED, updateCounterIncrementStepAfterDelay);
}

function* fetchCounterIncrementStep() {
    try {
        const { success, authorized, user } = yield select(state => state.loginresult);
        const { username } = user;
        if (success && authorized) {
            const response = yield call(getCounterIncrementStep, username);
            const counterIncrementStepresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
            yield put(COUNTER_INCREMENT_STEP_RECEIVE(counterIncrementStepresult));
        }
    } catch (error) {
        yield put(COUNTER_INCREMENT_STEP_FAILURE(error));
    }
}

function getCounterIncrementStep(username) {
    return axios.get('/api/counter/incrementstep/' + username);
}

function* updateCounterIncrementStep(action) {
    try {
        const { success, authorized, user } = yield select(state => state.loginresult);
        const { username } = user;
        if (success && authorized) {
            const counterIncrementStep = action.payload;
            const response = yield call(postCounterIncrementStep, { username, counterIncrementStep});
            const counterIncrementStepresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
            yield put(UPDATE_COUNTER_INCREMENT_STEP_RECEIVE(counterIncrementStepresult));
        }
    } catch (error) {
        yield put(UPDATE_COUNTER_INCREMENT_STEP_FAILURE(error));
    }
}

function postCounterIncrementStep(updatedCounterIncrementStep) {
    return axios.post('/api/counter/incrementstep/', updatedCounterIncrementStep);
}

function* updateCounterIncrementStepAfterDelay(action) {
    yield delay(2000);
    yield put(UPDATE_COUNTER_INCREMENT_STEP_REQUEST(action.payload));
}
