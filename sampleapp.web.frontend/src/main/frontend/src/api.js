import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const api = createApi({
    reducerPath: 'api',
    baseQuery: (...args) => {
        const api = args[1];
        const basename = api.getState().basename;
        return fetchBaseQuery({ baseUrl: basename + '/api' })(...args);
    },
    endpoints: (builder) => ({
        getDefaultlocale: builder.query({ query: () => '/defaultlocale' }),
        getLoginstate: builder.query({ query: (locale) => '/loginstate?locale=' + locale }),
        getAvailablelocales: builder.query({ query: () => '/availablelocales' }),
        getDisplaytexts: builder.query({ query: (locale) => '/displaytexts?locale=' + locale }),
        getAccounts: builder.query({ query: () => '/accounts' }),
        getCounterIncrementStep: builder.query({ query: (username) => '/counter/incrementstep/' + username }),
        getCounter: builder.query({ query: (username) => '/counter/' + username }),
        postLogin: builder.mutation({
            query: (body) => ({url: '/login', method: 'POST', body }),
            async onQueryStarted(body, { dispatch, queryFulfilled }) {
                try {
                    const { data: loginResult } = await queryFulfilled;
                    dispatch(api.util.updateQueryData('getLoginstate', body.locale, () => loginResult));
                } catch {}
            },
        }),
        getLogout: builder.mutation({ query: () => ({url: '/logout', method: 'GET' }) }),
        postCounterIncrementStep: builder.mutation({
            query: (body) => ({url: '/counter/incrementstep', method: 'POST', body }),
            async onQueryStarted(body, { dispatch, queryFulfilled }) {
                try {
                    const { data: updatedIncrementStep } = await queryFulfilled;
                    dispatch(api.util.updateQueryData('getCounterIncrementStep', body.username, () => updatedIncrementStep));
                } catch {}
            },
        }),
        getIncrementCounter: builder.mutation({
            query: (username) => ({url: '/counter/' + username + '/increment', method: 'GET' }),
            async onQueryStarted(username, { dispatch, queryFulfilled }) {
                try {
                    const { data: incrementResult } = await queryFulfilled;
                    dispatch(api.util.updateQueryData('getCounter', username, () => incrementResult));
                } catch {}
            },
        }),
        getDecrementCounter: builder.mutation({
            query: (username) => ({url: '/counter/' + username + '/decrement', method: 'GET' }),
            async onQueryStarted(username, { dispatch, queryFulfilled }) {
                try {
                    const { data: decrementResult } = await queryFulfilled;
                    dispatch(api.util.updateQueryData('getCounter', username, () => decrementResult));
                } catch {}
            },
        }),
    }),
});

export const {
    useGetDefaultlocaleQuery,
    useGetLoginstateQuery,
    useGetAvailablelocalesQuery,
    useGetDisplaytextsQuery,
    useGetAccountsQuery,
    useGetCounterIncrementStepQuery,
    useGetCounterQuery,
    usePostLoginMutation,
    useGetLogoutMutation,
    usePostCounterIncrementStepMutation,
    useGetDecrementCounterMutation,
    useGetIncrementCounterMutation,
} = api;
