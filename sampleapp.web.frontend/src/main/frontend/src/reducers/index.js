import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';
import username from './usernameReducer';
import password from './passwordReducer';
import loginresultat from './loginresultatReducer';
import accounts from './accountsReducer';
import locale from './localeReducer';
import availableLocales from './availableLocalesReducer';
import displayTexts from './displayTextsReducer';
import errors from './errorsReducer';

export default (history) => combineReducers({
    router: connectRouter(history),
    username,
    password,
    loginresultat,
    accounts,
    locale,
    availableLocales,
    displayTexts,
    errors,
});
