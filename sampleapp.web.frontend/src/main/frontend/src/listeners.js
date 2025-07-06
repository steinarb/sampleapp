import { createListenerMiddleware } from '@reduxjs/toolkit';
import { isAnyOf } from '@reduxjs/toolkit';
import { api } from './api';
import { VIS_KVITTERING, INCREMENT_STEP_FIELD_MODIFIED } from './reduxactions';

const listeners = createListenerMiddleware();

listeners.startListening({
    matcher: api.endpoints.postLogin.matchFulfilled,
    effect: (action, listenerApi) => {
        if (action.payload.success) {
            const basename = listenerApi.getState().basename;
            const originalRequestUrl = findReloadUrl(basename, action.payload.originalRequestUrl);
            window.location.href = originalRequestUrl;
        }
    }
})

function findReloadUrl(basename, originalRequestUrl) {
    // If originalRequestUrl is empty go to the top.
    // If originalRequest is /unauthorized go to the top and let shiro decide where to redirect to
    if (!originalRequestUrl || originalRequestUrl === '/unauthorized') {
        return basename + '/';
    }

    return basename + originalRequestUrl;
}

const isRejectedRequest = isAnyOf(
    api.endpoints.getCounterIncrementStep.matchRejected,
    api.endpoints.postCounterIncrementStep.matchRejected,
    api.endpoints.getCounter.matchRejected,
    api.endpoints.getIncrementCounter.matchRejected,
    api.endpoints.getDecrementCounter.matchRejected,
)

listeners.startListening({
    matcher: isRejectedRequest,
    effect: ({ payload }) => {
        const { originalStatus } = payload || {};
        const statusCode = parseInt(originalStatus);
        if (statusCode === 401 || statusCode === 403) {
            location.reload(true); // Will return to current location after successful login
        }
    }
})

listeners.startListening({
    matcher: api.endpoints.getLogout.matchFulfilled,
    effect: (action, listenerApi) => {
        if (!action.payload.suksess) {
            const basename = listenerApi.getState().basename;
            location.href = basename + '/'; // Setting app top location before going to login, to avoid ending up in "/unauthorized" after login
        }
    }
})

listeners.startListening({
    actionCreator: INCREMENT_STEP_FIELD_MODIFIED,
    effect: async (action, listenerApi) => {
        await listenerApi.delay(2000); // Wait 2sek from last change before saving modification to backend
        const username = listenerApi.getState().username;
        const counterIncrementStep = listenerApi.getState().counterIncrementStep;
        listenerApi.dispatch(api.endpoints.postCounterIncrementStep.initiate({ username, counterIncrementStep }));
    }
})

export default listeners;
