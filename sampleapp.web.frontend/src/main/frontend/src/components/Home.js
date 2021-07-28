import React from 'react';
import { Redirect } from 'react-router';
import { connect } from 'react-redux';
import { Container } from './bootstrap/Container';
import { LOGOUT_REQUEST } from '../actiontypes';


function Home(props) {
    const {
        loginresultat,
        accountCount,
        onLogout,
    } = props;
    const username = loginresultat.username;
    if (!loginresultat.authorized) {
        return <Redirect to="/sampleapp/unauthorized" />;
    }

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <a className="btn btn-primary left-align-cell" href="../.."><span className="oi oi-chevron-left" title="chevron left" aria-hidden="true"></span>&nbsp;Gå hjem!</a>
                <h1>Sampleapp</h1>
            </nav>
            <Container>
                <p>Hei {username}!</p>
                <p>Antall accounts: {accountCount}</p>
                <p><button className="btn btn-primary" onClick={onLogout}>Logg ut</button></p>
            </Container>
        </div>
    );
}

function mapStateToProps(state) {
    const { loginresultat } = state;
    const accountCount = state.accounts.length;
    return {
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
