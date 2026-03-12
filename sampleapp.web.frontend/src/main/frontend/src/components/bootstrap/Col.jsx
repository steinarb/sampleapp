import React from 'react';

export default function Col(props) {
    const { className } = props;
    const classes = className ? 'col ' + className : 'col';
    return (
        <div className={classes}>
            {props.children}
        </div>
    );
}
