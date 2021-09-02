import React from 'react';
import { Redirect } from 'react-router';
import { connect } from 'react-redux';
import { Container } from './bootstrap/Container';
import { LOGOUT_REQUEST } from '../actiontypes';
import Locale from './Locale';


function Home(props) {
    const {
        text,
        loginresultat,
        accountCount,
        onLogout,
    } = props;
    const username = loginresultat.username;
    if (!loginresultat.authorized) {
        return <Redirect to="/unauthorized" />;
    }

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <a className="btn btn-primary left-align-cell" href="../.."><span className="oi oi-chevron-left" title="chevron left" aria-hidden="true"></span>&nbsp;{text.gohome}!</a>
                <h1>Sampleapp</h1>
                <Locale />
            </nav>
            <Container>
                <p>{text.hi} {username}!</p>
                <p>{text.numberofaccounts}: {accountCount}</p>
                <p><button className="btn btn-primary" onClick={onLogout}>{text.logout}</button></p>
            </Container>
        </div>
    );
}

function mapStateToProps(state) {
    const { loginresultat } = state;
    const text = state.displayTexts;
    const accountCount = state.accounts.length;
    return {
        text,
        loginresultat,
        accountCount,
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onLogout: () => dispatch(LOGOUT_REQUEST()),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
