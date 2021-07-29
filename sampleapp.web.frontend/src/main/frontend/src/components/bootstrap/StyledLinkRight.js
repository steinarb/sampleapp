import React from 'react';
import { Link } from 'react-router-dom';
import { ChevronRight } from './ChevronRight';

export function StyledLinkRight(props) {
    const { className = '' } = props;
    return (
        <Link className={className + ' btn btn-block btn-primary mb-0 right-align-cell'} to={props.to} >
            {props.children} &nbsp;<ChevronRight/>
        </Link>
    );
}
