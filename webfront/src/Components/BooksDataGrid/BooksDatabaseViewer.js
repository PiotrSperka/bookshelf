import styles from "./BooksDatabaseViewer.module.css"
import BookGrid from "./BookGrid";
import { useRef } from "react";

const BooksDatabaseViewer = () => {
    const ref = useRef();

    return (
        <div className={ styles.main }>
            <BookGrid ref={ ref }/>
        </div>
    )
}

export default BooksDatabaseViewer;
