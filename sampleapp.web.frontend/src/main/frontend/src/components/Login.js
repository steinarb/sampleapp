import React, { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { LOGIN_REQUEST } from '../reduxactions';
import LoginMessage from './LoginMessage';

export default function Login() {
    const basename = useSelector(state => state.basename);
    const loginresult = useSelector(state => state.loginresult);
    const text = useSelector(state => state.displayTexts);
    const dispatch = useDispatch();
    const [ username, setUsername ] = useState('');
    const [ password, setPassword ] = useState('');

    if (loginresult.success) {
        const originalRequestUrl = findReloadUrl(basename, loginresult.originalRequestUrl);
        window.location.href = originalRequestUrl;
    }

    return (
        <div className="Login">
            <header>
                <div className="pb-2 mt-4 mb-2 border-bottom bg-light">
                    <h1>Sampleapp login</h1>
                    <p id="messagebanner"></p>
                </div>
            </header>
            <div className="container">
                <LoginMessage/>
                <form onSubmit={e => { e.preventDefault(); }}>
                    <div className="form-group row">
                        <label htmlFor="username" className="col-form-label col-3 mr-2">{text.username}:</label>
                        <div className="col-8">
                            <input
                                id="username"
                                className="form-control"
                                type="text" name="username"
                                value={username}
                                onChange={e => setUsername(e.target.value)} />
                        </div>
                    </div>
                    <div className="form-group row">
                        <label htmlFor="password" className="col-form-label col-3 mr-2">{text.password}:</label>
                        <div className="col-8">
                            <input
                                id="password" className="form-control"
                                type="password"
                                name="password"
                                value={password}
                                onChange={e => setPassword(e.target.value)}/>
                        </div>
                    </div>
                    <div className="form-group row">
                        <div className="offset-xs-3 col-xs-9">
                            <input
                                className="btn btn-primary"
                                type="submit" value="Login"
                                onClick={() => dispatch(LOGIN_REQUEST({ username, password: btoa(password) }))}/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
}

function findReloadUrl(basename, originalRequestUrl) {
    // If originalRequestUrl is empty go to the top.
    // If originalRequest is /unauthorized go to the top and let shiro decide where to redirect to
    if (!originalRequestUrl || originalRequestUrl === '/unauthorized') {
        return basename + '/';
    }

    return basename + originalRequestUrl;
}
