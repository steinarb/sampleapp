import React from 'react';
import { NavLink } from 'react-router';
import { useSelector, useDispatch } from 'react-redux';
import {
    useGetDefaultlocaleQuery,
    useGetLoginstateQuery,
    useGetDisplaytextsQuery,
    useGetCounterIncrementStepQuery,
    useGetCounterQuery,
    useGetDecrementCounterMutation,
    useGetIncrementCounterMutation,
} from '../api';
import Container from './bootstrap/Container';
import {
    INCREMENT_STEP_FIELD_MODIFIED,
    COUNTER_DECREMENT_REQUEST,
    COUNTER_INCREMENT_REQUEST,
} from '../reduxactions';
import Locale from './Locale';
import Minus from './bootstrap/Minus';
import Plus from './bootstrap/Plus';


export default function Counter() {
    const { isSuccess: defaultLocaleIsSuccess } = useGetDefaultlocaleQuery();
    const locale = useSelector(state => state.locale);
    const { data: loginresult = { user: {} }, isSuccess: loginresultIsSuccess } = useGetLoginstateQuery(locale, { skip: !defaultLocaleIsSuccess });
    const username = loginresult.user.username;
    const { data: text = [] } = useGetDisplaytextsQuery(locale, { skip: !defaultLocaleIsSuccess });
    useGetCounterIncrementStepQuery(username, { skip: !loginresultIsSuccess });
    const counterIncrementStep = useSelector(state => state.counterIncrementStep);
    const { data: counter = { counter: 0 } } = useGetCounterQuery(username, { skip: !loginresultIsSuccess });
    const [ getDecrementCounter ] = useGetDecrementCounterMutation();
    const [ getIncrementCounter ] = useGetIncrementCounterMutation();
    const onDecrementClicked = async () => { await getDecrementCounter(username) }
    const onIncrementClicked = async () => { await getIncrementCounter(username) }
    const dispatch = useDispatch();
    const firstname = loginresult.user.firstname;

    return (
        <div>
            <div className="home-menu pure-menu pure-menu-horizontal pure-menu-fixed">
                <h1 className="pure-menu-heading">{text.counter_for} {firstname}</h1>
                <ul className="pure-menu-list">
                    <li className="pure-menu-item"><NavLink to="/">{text.gohome}</NavLink></li>
                    <li className="pure-menu-item"><Locale /></li>
                </ul>
            </div>
            <div className="content-wrapper">
                <form className="pure-form pure-form-aligned" onSubmit={ e => { e.preventDefault(); }}>
                    <div className="pure-control-group">
                        <label htmlFor="amount">{text.counterIncrementStep}</label>
                        <input
                            id="amount"
                            type="number"
                            pattern="\d+"
                            value={counterIncrementStep}
                            onChange={e => dispatch(INCREMENT_STEP_FIELD_MODIFIED(e.target.value))} />
                    </div>
                </form>
                <div className="pure-control-group">
                    <button className="pure-button pure-button-primary" onClick={onDecrementClicked}><Minus/></button>
                    <input readOnly value={counter.counter}/>
                    <button className="pure-button pure-button-primary" onClick={onIncrementClicked}><Plus/></button>
                </div>
            </div>
        </div>
    );
}
