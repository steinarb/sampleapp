import React, { Component } from 'react';

export function FormField(props) {
    return (
        <div className="col-7">
            {props.children}
        </div>
    );
}
