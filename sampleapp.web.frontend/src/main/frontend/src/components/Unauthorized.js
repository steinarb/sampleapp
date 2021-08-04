import React from 'react';
import { Redirect } from 'react-router';
import { connect } from 'react-redux';
import {
    LOGOUT_REQUEST,
} from '../actiontypes';
import { Container } from './bootstrap/Container';


function Unauthorized(props) {
    const { username, loginresultat, text, onLogout } = props;
    if (!loginresultat.suksess) {
        return <Redirect to="/sampleapp/login" />;
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
                            <button className="btn btn-primary" onClick={onLogout}>{text.logout}</button>
                        </div>
                    </div>
                </form>
            </Container>
        </div>
    );
}

const mapStateToProps = state => {
    const { username, loginresultat } = state;
    const text = state.displayTexts;
    return {
        username,
        loginresultat,
        text,
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onLogout: () => dispatch(LOGOUT_REQUEST()),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Unauthorized);
