import React from 'react';
import { Redirect } from 'react-router';
import { NavLink } from 'react-router-dom';
import { connect } from 'react-redux';
import { Container } from './bootstrap/Container';
import {
    COUNTER_INCREMENT_STEP_MODIFY,
    COUNTER_DECREMENT_REQUEST,
    COUNTER_INCREMENT_REQUEST,
} from '../actiontypes';
import Locale from './Locale';
import Minus from './bootstrap/Minus';
import Plus from './bootstrap/Plus';


function Counter(props) {
    const {
        text,
        loginresultat,
        counterIncrementStep,
        counter,
        onCounterIncrementStepModify,
        decrementCounter,
        incrementCounter,
    } = props;
    const firstname = loginresultat.user.firstname;
    if (!loginresultat.authorized) {
        return <Redirect to="/unauthorized" />;
    }

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <NavLink className="btn btn-primary left-align-cell" to="/"><span className="oi oi-chevron-left" title="chevron left" aria-hidden="true"></span>&nbsp;{text.gohome}!</NavLink>
                <h1>{text.counter_for} {firstname}</h1>
                <Locale />
            </nav>
            <Container>
                <form onSubmit={ e => { e.preventDefault(); }}>
                    <div className="form-group row">
                        <label htmlFor="amount" className="col-form-label col-5">{text.counterIncrementStep}</label>
                        <div className="col-7">
                            <input id="amount" className="form-control" type="number" pattern="\d+" value={counterIncrementStep} onChange={onCounterIncrementStepModify} />
                        </div>
                    </div>
                </form>
                <div className="btn-group">
                    <button className="btn btn-secondary" onClick={decrementCounter}><Minus/></button>
                    <input disable="true" className="btn btn-secondary" value={counter}/>
                    <button className="btn btn-secondary" onClick={incrementCounter}><Plus/></button>
                </div>
            </Container>
        </div>
    );
}

function mapStateToProps(state) {
    const {
        loginresultat,
        counterIncrementStep,
        counter,
    } = state;
    return {
        text: state.displayTexts,
        loginresultat,
        counterIncrementStep,
        counter,
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onCounterIncrementStepModify: e => dispatch(COUNTER_INCREMENT_STEP_MODIFY(e.target.value)),
        decrementCounter: () => dispatch(COUNTER_DECREMENT_REQUEST()),
        incrementCounter: () => dispatch(COUNTER_INCREMENT_REQUEST()),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Counter);
