import React from 'react';

export function Container(props) {
    return (
        <div className="container">
            {props.children}
        </div>
    );
}
