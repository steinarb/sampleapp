import React, { Component } from 'react';

export function ContainerFluid(props) {
    return (
        <div className="col">
            {props.children}
        </div>
    );
}
