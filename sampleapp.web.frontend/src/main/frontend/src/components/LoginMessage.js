import React from 'react';
import { useSelector } from 'react-redux';
import {
    useGetDefaultlocaleQuery,
    useGetLoginstateQuery,
} from '../api';

export default function LoginMessage() {
    const { isSuccess: defaultLocaleIsSuccess } = useGetDefaultlocaleQuery();
    const locale = useSelector(state => state.locale);
    const { data: loginresult = {} } = useGetLoginstateQuery(locale, { skip: !defaultLocaleIsSuccess });

    if (!loginresult.errormessage) {
        return null;
    }

    return (
        <div className='alert alert-warning' role='alert'>
            {loginresult.errormessage}
        </div>
    );
}
