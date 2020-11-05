package concurency;

/**
 * Created by X13 on 14.02.2017.
 */


/**
 *
 * @author Madalin Ilie
 */
public interface LinkHandler {

    /**
     * Places the link in the queue
     * @param link
     * @throws Exception
     */
    void queueLink(String link) throws Exception;

    /**
     * Returns the number of visited links
     * @return
     */
    int size();

    /**
     * Checks if the link was already visited
     * @param link
     * @return
     */
    boolean visited(String link);

    /**
     * Marks this link as visited
     * @param link
     */
    void addVisited(String link);
}