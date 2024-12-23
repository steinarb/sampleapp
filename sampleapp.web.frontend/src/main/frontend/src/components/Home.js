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
import ChevronLeft from './bootstrap/ChevronLeft';


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
            <nav className="navbar navbar-light bg-light">
                <a className="btn btn-primary left-align-cell" href="../..">
                    <ChevronLeft />&nbsp;{text.gohome}!
                </a>
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
                <p><button className="btn btn-primary" onClick={onLogoutClicked}>{text.logout}</button></p>
            </Container>
        </div>
    );
}
