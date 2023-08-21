import styles from "./CzechMedievalSourcesScraper.module.css"
import { Alert, Box, Button, TextField } from "@mui/material";
import { FormattedMessage } from "react-intl";
import DownloadIcon from '@mui/icons-material/Download';
import { useEffect, useRef, useState } from "react";
import { useApi } from "../../Services/GenericServiceHook";
import { getAddJobParams } from "../../Services/BookScrapingApi";

const CzechMedievalSourcesScraper = props => {
    const formRef = useRef();
    const scrapingApi = useApi();
    const [ urlInvalid, setUrlInvalid ] = useState( false );

    const isValidUrl = urlString => {
        var urlPattern = new RegExp( '^(https?:\\/\\/)?' + // validate protocol
            '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // validate domain name
            '((\\d{1,3}\\.){3}\\d{1,3}))' + // validate OR ip (v4) address
            '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // validate port and path
            '(\\?[;&a-z\\d%_.~+=-]*)?' + // validate query string
            '(\\#[-a-z\\d_]*)?$', 'i' ); // validate fragment locator
        return !!urlPattern.test( urlString );
    }

    const formSubmitted = ( event ) => {
        event.preventDefault();

        const formData = new FormData( event.target );
        const url = formData.get( "book-url" );
        const jobData = { bookScraperType: "CZECH_MEDIEVAL_SOURCES", inputData: url }
        const urlValid = isValidUrl( url );

        setUrlInvalid( !urlValid )

        if ( urlValid ) {
            scrapingApi.request( getAddJobParams( jobData ) )
        }

        formRef.current.reset();
    }

    useEffect( () => {
        if ( scrapingApi.data ) {
            if ( props.onRequestSent ) {
                props.onRequestSent();
            }
        }
    }, [ scrapingApi.data ] );

    return (
        <Box>
            <Box>
                <p><FormattedMessage id={ "book-scraping.czech-medieval-sources.info-1" }/><a
                    href={ "https://sources.cms.flu.cas.cz" }
                    target={ "_blank" } rel="noreferrer">sources.cms.flu.cas.cz</a><FormattedMessage
                    id={ "book-scraping.czech-medieval-sources.info-2" }/></p>
                <p><FormattedMessage id={ "book-scraping.czech-medieval-sources.info-3" }/></p>
                <p><FormattedMessage id={ "book-scraping.czech-medieval-sources.info-4" }/></p>
            </Box>
            <form className={ styles.form } onSubmit={ formSubmitted } ref={ formRef }>
                { urlInvalid &&
                    <Alert severity={ "error" }><FormattedMessage id="book-scraping.url-invalid"/></Alert> }
                <TextField name={ "book-url" } label={ <FormattedMessage id="book-scraping.url"/> }
                           variant={ "standard" }/><br/>
                <Button className={ styles.submitButton } variant={ "contained" } type={ "submit" }
                        startIcon={ <DownloadIcon/> }><FormattedMessage id="book-scraping.scrape"/></Button>
            </form>
        </Box>
    )
}

export default CzechMedievalSourcesScraper;
