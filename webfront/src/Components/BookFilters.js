import "./BookFilters.css"
import {Box, Grid, TextField} from "@mui/material";
import {useEffect, useState} from "react";
import { FormattedMessage } from "react-intl";

const BookFilters = (props) => {
    const [filterState, setFilterState] = useState({author: "", title: "", release: "", signature: ""});
    const [lastEventFilterState, setLastEventFilterState] = useState({});

    useEffect(() => {
        // Check if event data really changed
        if (shallowEqual(lastEventFilterState, filterState)) {
            return;
        }

        setLastEventFilterState(filterState);

        if (props.onFilterChanged) {
            props.onFilterChanged(filterState);
        }
    }, [filterState, lastEventFilterState, props])

    const shallowEqual = (object1, object2) => {
        const keys1 = Object.keys(object1);
        const keys2 = Object.keys(object2);

        if (keys1.length !== keys2.length) {
            return false;
        }

        for (let key of keys1) {
            if (object1[key] !== object2[key]) {
                return false;
            }
        }

        return true;
    }

    const onAuthorFilterChanged = (event) => {
        if (event.target.value !== filterState.author) {
            setFilterState(prevState => ({...prevState, author: event.target.value}))
        }
    }

    const onTitleFilterChanged = (event) => {
        if (event.target.value !== filterState.title) {
            setFilterState(prevState => ({...prevState, title: event.target.value}))
        }
    }

    const onReleaseFilterChanged = (event) => {
        if (event.target.value !== filterState.release) {
            setFilterState(prevState => ({...prevState, release: event.target.value}))
        }
    }

    const onSignatureFilterChanged = (event) => {
        if (event.target.value !== filterState.signature) {
            setFilterState(prevState => ({...prevState, signature: event.target.value}))
        }
    }

    return (
        <Box>
            <Grid container>
                <Grid className={"inputFieldGrid"} xs={3}>
                    <TextField className={"inputField"} name={"author"} label={<FormattedMessage id="filters.author"/>} variant={"standard"}
                               onChange={onAuthorFilterChanged}/>
                </Grid>
                <Grid className={"inputFieldGrid"} xs={3}>
                    <TextField className={"inputField"} name={"title"} label={<FormattedMessage id="filters.title"/>} variant={"standard"}
                               onChange={onTitleFilterChanged}/>
                </Grid>
                <Grid className={"inputFieldGrid"} xs={3}>
                    <TextField className={"inputField"} name={"release"} label={<FormattedMessage id="filters.release"/>} variant={"standard"}
                               onChange={onReleaseFilterChanged}/>
                </Grid>
                <Grid className={"inputFieldGrid"} xs={3}>
                    <TextField className={"inputField"} name={"signature"} label={<FormattedMessage id="filters.signature"/>} variant={"standard"}
                               onChange={onSignatureFilterChanged}/>
                </Grid>
            </Grid>
        </Box>
    )
}

export default BookFilters;
