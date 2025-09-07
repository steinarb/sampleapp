import React from 'react';
import { NavLink } from 'react-router';
import { useSelector } from 'react-redux';
import {
    useGetDefaultlocaleQuery,
    useGetLoginstateQuery,
    useGetDisplaytextsQuery,
    useGetAccountsQuery,
    useGetLogoutMutation,
} from '../api';
import Container from './bootstrap/Container';
import Locale from './Locale';


export default function Home() {
    const { isSuccess: defaultLocaleIsSuccess } = useGetDefaultlocaleQuery();
    const locale = useSelector(state => state.locale);
    const { data: loginresult = {} } = useGetLoginstateQuery(locale, { skip: !defaultLocaleIsSuccess });
    const { data: accounts = [] } = useGetAccountsQuery();
    const { data: text = [] } = useGetDisplaytextsQuery(locale, { skip: !defaultLocaleIsSuccess });
    const accountCount = accounts.length;
    const { username, firstname, lastname, email } = loginresult.user || {};
    const [ getLogout ] = useGetLogoutMutation();
    const onLogoutClicked = async () => { await getLogout() }

    return (
        <div>
            <div className="home-menu pure-menu pure-menu-horizontal pure-menu-fixed">
                <h1 className="pure-menu-heading">Sampleapp</h1>
                <ul className="pure-menu-list">
                    <li className="pure-menu-item"><NavLink to="/counter">{text.counter}</NavLink></li>
                    <li className="pure-menu-item"><a className="pure-menu-link" href="../..">{text.gohome}</a></li>
                    <li className="pure-menu-item"><Locale /></li>
                </ul>
            </div>
            <div className="content-wrapper">
                <p>{text.hi} {firstname}!</p>
                <p>{text.numberofaccounts}: {accountCount}</p>
                <p>{text.logged_in_user_info}</p>
                <table className="pure-table pure-table-bordered">
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
                <p><button className="pure-button pure-button-primary" onClick={onLogoutClicked}>{text.logout}</button></p>
            </div>
        </div>
    );
}
