import { Box, Card, CardContent, Tab, Tabs } from "@mui/material";
import { useUserContext } from "../../UserContextProvider";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { FormattedMessage } from "react-intl";
import styles from "../Admin/SystemAdmin.module.css";
import CzechMedievalSourcesScraper from "./CzechMedievalSourcesScraper";
import ScrapedBooksGrid from "./ScrapedBooksGrid";

const BookScraping = () => {
    const { hasRole, isLoggedIn } = useUserContext();
    const navigate = useNavigate();
    const [ tabValue, setTabValue ] = useState( 0 );
    const [ jobsRefreshTrigger, setJobsRefreshTrigger ] = useState( 0 );

    useEffect( () => {
        if ( !isLoggedIn || ( !hasRole( "admin" ) && !hasRole( "user" ) ) ) {
            navigate( "/" )
        }

    }, [ isLoggedIn ] )

    const onJobsListChanged = () => {
        setJobsRefreshTrigger( prevState => prevState + 1 )
    }

    return (
        <Box className={ styles.main }>
            <Box sx={ { borderBottom: 1, borderColor: 'divider' } }>
                <Tabs value={ tabValue } onChange={ ( e, val ) => setTabValue( val ) } variant={ "scrollable" }
                      scrollButtons={ "auto" } allowScrollButtonsMobile>
                    <Tab label={ <FormattedMessage id="book-scraping.czech-medieval-sources"/> }/>
                </Tabs>
            </Box>
            { tabValue === 0 && <Card className={ styles.cardFullWidth }>
                <CardContent>
                    <CzechMedievalSourcesScraper onRequestSent={ onJobsListChanged }/>
                </CardContent>
            </Card> }

            <ScrapedBooksGrid refreshTrigger={ jobsRefreshTrigger }/>
        </Box>
    )
}

export default BookScraping;
