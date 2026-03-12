import React from 'react';
import { useSelector } from 'react-redux';
import {
    useGetDefaultlocaleQuery,
    useGetLoginstateQuery,
    useGetDisplaytextsQuery,
    useGetLogoutMutation,
} from '../api';
import Container from './bootstrap/Container';
import Locale from './Locale';


export default function Unauthorized() {
    const { isSuccess: defaultLocaleIsSuccess } = useGetDefaultlocaleQuery();
    const locale = useSelector(state => state.locale);
    const { data: loginresult = { user: {} } } = useGetLoginstateQuery(locale, { skip: !defaultLocaleIsSuccess });
    const { data: text = [] } = useGetDisplaytextsQuery(locale, { skip: !defaultLocaleIsSuccess });
    const username = loginresult.user.username;
    const [ getLogout ] = useGetLogoutMutation();
    const onLogoutClicked = async () => { await getLogout() }

    return (
        <div>
            <div className="home-menu pure-menu pure-menu-horizontal pure-menu-fixed">
                <h1 className="pure-menu-heading">{text.noaccess}</h1>
                <ul className="pure-menu-list">
                    <li className="pure-menu-item"><a className="pure-menu-link" href="../..">{text.gohome}</a></li>
                    <li className="pure-menu-item"><Locale /></li>
                </ul>
            </div>
            <div className="content-wrapper">
                <p>{text.hi} {username}! {text.noaccessmessage1}</p>
                <p>{text.noaccessmessage2}</p>
                <form className="pure-form pure-form-aligned" onSubmit={ e => { e.preventDefault(); }}>
                    <div className="pure-control-group">
                        <button className="pure-button pure-button-primary" onClick={onLogoutClicked}>{text.logout}</button>
                    </div>
                </form>
            </div>
        </div>
    );
}
