import React from 'react';
import { Redirect } from 'react-router';
import { useSelector, useDispatch } from 'react-redux';
import { LOGOUT_REQUEST } from '../actiontypes';
import { Container } from './bootstrap/Container';


export default function Unauthorized() {
    const loginresult = useSelector(state => state.loginresult);
    const text = useSelector(state => state.displayTexts);
    const username = loginresult.user.username;
    const dispatch = useDispatch();

    if (!loginresult.success) {
        return <Redirect to="/login" />;
    }

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <a className="btn btn-primary left-align-cell" href="../.."><span className="oi oi-chevron-left" title="chevron left" aria-hidden="true"></span>&nbsp;{text.gohome}!</a>
                <h1>{text.noaccess}</h1>
                <div className="col-sm-2"></div>
            </nav>
            <Container>
                <p>{text.hi} {username}! {text.noaccessmessage1}</p>
                <p>{text.noaccessmessage2}</p>
                <form onSubmit={ e => { e.preventDefault(); }}>
                    <div className="form-group row">
                        <div className="col-5"/>
                        <div className="col-7">
                            <button className="btn btn-primary" onClick={() => dispatch(LOGOUT_REQUEST())}>{text.logout}</button>
                        </div>
                    </div>
                </form>
            </Container>
        </div>
    );
}
