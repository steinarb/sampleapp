import React from 'react';

export function ContainerFluid (props) {
    return (
        <div className="container-fluid">
            {props.children}
        </div>
    );
}
