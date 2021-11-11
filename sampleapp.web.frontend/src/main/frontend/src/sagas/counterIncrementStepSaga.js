import { takeLatest, call, put, select, delay } from 'redux-saga/effects';
import axios from 'axios';
import {
    COUNTER_INCREMENT_STEP_REQUEST,
    COUNTER_INCREMENT_STEP_RECEIVE,
    COUNTER_INCREMENT_STEP_ERROR,
    LOGIN_RECEIVE,
    LOGINSTATE_RECEIVE,
    UPDATE_COUNTER_INCREMENT_STEP_REQUEST,
    UPDATE_COUNTER_INCREMENT_STEP_RECEIVE,
    UPDATE_COUNTER_INCREMENT_STEP_ERROR,
    COUNTER_INCREMENT_STEP_MODIFY,
} from '../actiontypes';

function getCounterIncrementStep(username) {
    return axios.get('/api/counter/incrementstep/' + username);
}

function* fetchCounterIncrementStep() {
    try {
        const { suksess, authorized, user } = yield select(state => state.loginresultat);
        const { username } = user;
        if (suksess && authorized) {
            const response = yield call(getCounterIncrementStep, username);
            const counterIncrementStepresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
            yield put(COUNTER_INCREMENT_STEP_RECEIVE(counterIncrementStepresult));
        }
    } catch (error) {
        yield put(COUNTER_INCREMENT_STEP_ERROR(error));
    }
}

function postCounterIncrementStep(updatedCounterIncrementStep) {
    return axios.post('/api/counter/incrementstep/', updatedCounterIncrementStep);
}

function* updateCounterIncrementStep(action) {
    try {
        const { suksess, authorized, user } = yield select(state => state.loginresultat);
        const { username } = user;
        if (suksess && authorized) {
            const counterIncrementStep = action.payload;
            const response = yield call(postCounterIncrementStep, { username, counterIncrementStep});
            const counterIncrementStepresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
            yield put(UPDATE_COUNTER_INCREMENT_STEP_RECEIVE(counterIncrementStepresult));
        }
    } catch (error) {
        yield put(UPDATE_COUNTER_INCREMENT_STEP_ERROR(error));
    }
}

function* updateCounterIncrementStepAfterDelay(action) {
    yield delay(2000);
    yield put(UPDATE_COUNTER_INCREMENT_STEP_REQUEST(action.payload));
}

export default function* counterIncrementStepSaga() {
    yield takeLatest(COUNTER_INCREMENT_STEP_REQUEST, fetchCounterIncrementStep);
    yield takeLatest(LOGIN_RECEIVE, fetchCounterIncrementStep);
    yield takeLatest(LOGINSTATE_RECEIVE, fetchCounterIncrementStep);
    yield takeLatest(UPDATE_COUNTER_INCREMENT_STEP_REQUEST, updateCounterIncrementStep);
    yield takeLatest(COUNTER_INCREMENT_STEP_MODIFY, updateCounterIncrementStepAfterDelay);
}
