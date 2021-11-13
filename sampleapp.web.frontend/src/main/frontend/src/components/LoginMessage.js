import React from 'react';
import { connect } from 'react-redux';

function LoginMessage(props) {
    const { loginresult } = props;
    if (!loginresult.errormessage) {
        return null;
    }

    return (
        <div className='alert alert-warning' role='alert'>
            {loginresult.errormessage}
        </div>
    );
}

function mapStateToProps(state) {
    const { loginresult } = state;
    return {
        loginresult,
    };
}

export default connect(mapStateToProps)(LoginMessage);
