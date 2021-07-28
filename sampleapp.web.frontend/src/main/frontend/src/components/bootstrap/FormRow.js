import React from 'react';

export function FormRow(props) {
    return (
        <div className="form-group row">
            {props.children}
        </div>
    );
}
