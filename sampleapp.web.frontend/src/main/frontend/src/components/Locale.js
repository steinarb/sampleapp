import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useGetDefaultlocaleQuery, useGetAvailablelocalesQuery } from '../api';
import { SELECT_LOCALE } from '../reduxactions';

export default function Locale(props) {
    const { className } = props;
    useGetDefaultlocaleQuery();
    const locale = useSelector(state => state.locale);
    const { data: availableLocales = [] } = useGetAvailablelocalesQuery();
    const dispatch = useDispatch();

    return (
        <select className={className} onChange={e => dispatch(SELECT_LOCALE(e.target.value))} value={locale}>
            {availableLocales.map((l) => <option key={'locale_' + l.code} value={l.code}>{l.displayLanguage}</option>)}
        </select>
    );
}
