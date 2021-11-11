import React from 'react';
import { Redirect } from 'react-router';
import { NavLink } from 'react-router-dom';
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
    const { username, firstname, lastname, email } = loginresultat.user;
    if (!loginresultat.authorized) {
        return <Redirect to="/unauthorized" />;
    }

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <a className="btn btn-primary left-align-cell" href="../.."><span className="oi oi-chevron-left" title="chevron left" aria-hidden="true"></span>&nbsp;{text.gohome}!</a>
                <h1>Sampleapp</h1>
                <NavLink className="btn btn-primary" to="/counter">{text.counter}</NavLink>
                <Locale />
            </nav>
            <Container>
                <p>{text.hi} {firstname}!</p>
                <p>{text.numberofaccounts}: {accountCount}</p>
                <p>{text.logged_in_user_info}</p>
                <table className="table">
                    <tbody>
                        <tr>
                            <th>{text.username}</th>
                            <td>{username}</td>
                        </tr>
                        <tr>
                            <th>{text.firstname}</th>
                            <td>{firstname}</td>
                        </tr>
                        <tr>
                            <th>{text.lastname}</th>
                            <td>{lastname}</td>
                        </tr>
                        <tr>
                            <th>{text.email}</th>
                            <td>{email}</td>
                        </tr>
                    </tbody>
                </table>
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
