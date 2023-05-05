import styles from "./BottomBar.module.css"
import { Box, Typography } from "@mui/material";

const BottomBar = () => {
    return (
        <Box className={styles.bar}>
            <Typography className={styles.version}>
                v{process.env.REACT_APP_VERSION}
            </Typography>
        </Box>
    )
}

export default BottomBar;
