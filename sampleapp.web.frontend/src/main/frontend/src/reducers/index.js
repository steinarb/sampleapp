import { combineReducers } from 'redux';
import { api } from '../api';
import counterIncrementStep from './counterIncrementStepReducer';
import username from './usernameReducer';
import locale from './localeReducer';
import basename from './basenameReducer';

const combinedReducers = (routerReducer) => combineReducers({
    router: routerReducer,
    [api.reducerPath]: api.reducer,
    counterIncrementStep,
    username,
    locale,
    basename,
});

export default combinedReducers;
