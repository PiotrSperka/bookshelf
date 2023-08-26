import * as React from 'react';
import PropTypes from 'prop-types';
import CircularProgress from '@mui/material/CircularProgress';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

export default function CircularProgressWithLabel( props ) {
    return (
        <Box display='flex' justifyContent='center' alignItems='center'>
            <CircularProgress variant="determinate" { ...props } />
            <Typography position={ "absolute" } variant="caption" color="text.secondary">
                { `${ Math.round( props.value ) }%` }
            </Typography>
        </Box>
    );
}

CircularProgressWithLabel.propTypes = {
    /**
     * The value of the progress indicator for the determinate variant.
     * Value between 0 and 100.
     * @default 0
     */
    value: PropTypes.number.isRequired,
};
