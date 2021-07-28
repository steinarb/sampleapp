import React from 'react';
import { connect } from 'react-redux';

function LoginMessage(props) {
    const { loginresultat } = props;
    if (!loginresultat.feilmelding) {
        return null;
    }

    return (
        <div className='alert alert-warning' role='alert'>
            {loginresultat.feilmelding}
        </div>
    );
}

function mapStateToProps(state) {
    const { loginresultat } = state;
    return {
        loginresultat,
    };
}

export default connect(mapStateToProps)(LoginMessage);
