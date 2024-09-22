import { combineReducers } from 'redux';
import loginresult from './loginresultReducer';
import accounts from './accountsReducer';
import counterIncrementStep from './counterIncrementStepReducer';
import counter from './counterReducer';
import locale from './localeReducer';
import availableLocales from './availableLocalesReducer';
import displayTexts from './displayTextsReducer';
import errors from './errorsReducer';
import basename from './basenameReducer';

const combinedReducers = (routerReducer) => combineReducers({
    router: routerReducer,
    loginresult,
    accounts,
    counterIncrementStep,
    counter,
    locale,
    availableLocales,
    displayTexts,
    errors,
    basename,
});

export default combinedReducers;
