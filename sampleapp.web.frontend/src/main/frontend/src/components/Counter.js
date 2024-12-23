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
import ChevronLeft from './bootstrap/ChevronLeft';
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
            <nav className="navbar navbar-light bg-light">
                <NavLink className="btn btn-primary left-align-cell" to="/">
                    <ChevronLeft />&nbsp;{text.gohome}!
                </NavLink>
                <h1>{text.counter_for} {firstname}</h1>
                <Locale />
            </nav>
            <Container>
                <form onSubmit={ e => { e.preventDefault(); }}>
                    <div className="form-group row">
                        <label htmlFor="amount" className="col-form-label col-5">{text.counterIncrementStep}</label>
                        <div className="col-7">
                            <input
                                id="amount"
                                className="form-control"
                                type="number"
                                pattern="\d+"
                                value={counterIncrementStep}
                                onChange={e => dispatch(INCREMENT_STEP_FIELD_MODIFIED(e.target.value))} />
                        </div>
                    </div>
                </form>
                <div className="btn-group">
                    <button className="btn btn-secondary" onClick={onDecrementClicked}><Minus/></button>
                    <input readOnly className="btn btn-secondary" value={counter.counter}/>
                    <button className="btn btn-secondary" onClick={onIncrementClicked}><Plus/></button>
                </div>
            </Container>
        </div>
    );
}
