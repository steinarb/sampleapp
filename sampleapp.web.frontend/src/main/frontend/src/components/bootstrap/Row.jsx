import React from 'react';

export default function Row(props) {
    const { className } = props;
    const classes = className ? 'row ' + className : 'row';
    return (
        <div className={classes}>
            {props.children}
        </div>
    );
}
