import React from 'react';
import { Link } from 'react-router';
import { ChevronRight } from './ChevronRight';

export default function StyledLinkRight(props) {
    const { className = '' } = props;
    return (
        <Link className={className + ' btn btn-block btn-primary mb-0 right-align-cell'} to={props.to} >
            {props.children} &nbsp;<ChevronRight/>
        </Link>
    );
}
