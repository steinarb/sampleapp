import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import {
    useGetDefaultlocaleQuery,
    useGetLoginstateQuery,
    usePostLoginMutation,
    useGetDisplaytextsQuery,
} from '../api';
import LoginMessage from './LoginMessage';

export default function Login() {
    const [ username, setUsername ] = useState('');
    const [ password, setPassword ] = useState('');
    const basename = useSelector(state => state.basename);
    const { isSuccess: defaultLocaleIsSuccess } = useGetDefaultlocaleQuery();
    const locale = useSelector(state => state.locale);
    const { data: loginresult = {} } = useGetLoginstateQuery(locale, { skip: !defaultLocaleIsSuccess });
    const { data: text = [] } = useGetDisplaytextsQuery(locale, { skip: !defaultLocaleIsSuccess });
    const [ postLogin ] = usePostLoginMutation();
    const onLoginClicked = async () => { await postLogin({ username, password: btoa(password), locale }) }

    return (
        <div className="Login">
            <div className="home-menu pure-menu pure-menu-horizontal pure-menu-fixed">
                <a className="pure-menu-heading">Sampleapp login</a>
            </div>
            <div className="content-wrapper">
                <LoginMessage/>
                <form className="pure-form pure-form-aligned" onSubmit={e => { e.preventDefault(); }}>
                    <div className="pure-control-group">
                        <label htmlFor="username">{text.username}:</label>
                        <input id="username" type="text" name="username" autoComplete="username" value={username} onChange={e => setUsername(e.target.value)} />
                    </div>
                    <div className="pure-control-group">
                        <label htmlFor="password">{text.password}:</label>
                        <input id="password" type="password" name="password" autoComplete="current-password" value={password} onChange={e => setPassword(e.target.value)}/>
                    </div>
                    <div className="pure-control-group">
                        <input className="pure-button pure-button-primary" type="submit" value="Login" onClick={onLoginClicked}/>
                    </div>
                </form>
            </div>
        </div>
    );
}
