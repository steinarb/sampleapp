import React from 'react';
import { useSelector } from 'react-redux';

export default function LoginMessage() {
    const loginresult = useSelector(state => state.loginresult);

    if (!loginresult.errormessage) {
        return null;
    }

    return (
        <div className='alert alert-warning' role='alert'>
            {loginresult.errormessage}
        </div>
    );
}
