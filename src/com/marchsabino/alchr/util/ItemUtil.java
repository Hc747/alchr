package com.marchsabino.alchr.util;

/**
 * Performs functions over items.  For example, you can find the "net profit" of alching a certain item, among
 * other things.
 *
 * @see Item
 * @author Marcello Sabino
 */
public interface ItemUtil {

    /**
     * The total amount of GP lost or gained by alching an item.
     * This takes into account money spent (price and nature rune) and GP you get from alching the item.
     *
     * @param highAlch true if you are high alching this item, false if you are low alching this item.
     * @return the total amunt of GP lost or gained by alching an item.
     */
    int netProfit(boolean highAlch);
}
