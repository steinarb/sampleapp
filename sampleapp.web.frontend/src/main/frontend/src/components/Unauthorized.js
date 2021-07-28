import React from 'react';
import { Redirect } from 'react-router';
import { connect } from 'react-redux';
import {
    LOGOUT_REQUEST,
} from '../actiontypes';
import { Container } from './bootstrap/Container';


function Unauthorized(props) {
    const { username, loginresultat, onLogout } = props;
    if (!loginresultat.suksess) {
        return <Redirect to="/sampleapp/login" />;
    }

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <a className="btn btn-primary left-align-cell" href="../.."><span className="oi oi-chevron-left" title="chevron left" aria-hidden="true"></span>&nbsp;Gå hjem!</a>
                <h1>Ingen tilgang</h1>
                <div className="col-sm-2"></div>
            </nav>
            <Container>
                <p>Hei {username}! Du har ikke tilgang til denne applikasjonen</p>
                <p>Klikk &quot;Gå hjem&quot; for å navigere ut av applikasjonen, eller logg ut for å logge inn med en bruker som har tilgang</p>
                <form onSubmit={ e => { e.preventDefault(); }}>
                    <div className="form-group row">
                        <div className="col-5"/>
                        <div className="col-7">
                            <button className="btn btn-primary" onClick={onLogout}>Logg ut</button>
                        </div>
                    </div>
                </form>
            </Container>
        </div>
    );
}

const mapStateToProps = state => {
    const { username, loginresultat } = state;
    return {
        username,
        loginresultat,
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onLogout: () => dispatch(LOGOUT_REQUEST()),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Unauthorized);
