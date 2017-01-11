package com.marchsabino.alchr.util;

import com.marchsabino.alchr.net.Connection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * The Item class.
 * An item is anything and everything that is on the grand exchange.  This includes: runes, armour, potions, bonds, etc.
 *
 * @author Marcello Sabino
 */
public class Item implements ItemUtil, Comparable<Item> {

    /** The nature rune's item id. */
    public final static int NATURE_RUNE_ID = 561;
    /** The nature rune's object reference. */
    public final static Item NATURE_RUNE = new Item(NATURE_RUNE_ID);

    /** The connection to the GE database. */
    private Connection connection;
    /** The item's id. */
    private int id;
    /** The item's current price. */
    private int currentPrice;
    /** The most amount of items of this you can buy at one time in GE. */
    private int buyLimit;
    /** The amount of GP you get when high alching this item. */
    private int highAlch;
    /** The amount of GP you get when low alching this item. */
    private int lowAlch;
    /** The item's name. */
    private String name;
    /** The item's image link. */
    private String image;

    /**
     * Creates a new item with the specified item id.
     * for example if you wanted to create an "Abyssal Whip" this id would be 4151.
     *
     * @param id the desired item's id.
     */
    public Item(int id) {
        this.id = id;
        try {
            this.connection = new Connection(new URL(Connection.BASE_URL + id));    // create a connection
            fillData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new item with the specified item name.
     * for example if you wanted to create an "Abyssal Whip" this itemName would be "abyssal whip"
     *
     * @param name the desired item's name.
     */
    public Item(String name) {
        this(getIdFromName(name));
    }

    /**
     * Fills the item with the correct GE data.
     * Data being filled is the items name, current price, and it's image.
     * @throws IOException
     */
    private void fillData() throws IOException {
        URLConnection connection = this.connection.getURL().openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();

        while ((inputLine = reader.readLine()) != null) {
            stringBuilder.append(inputLine);
        }

        reader.close();
        parseJSON(stringBuilder.toString());    // get the data we want.
    }

    /**
     * Parses the JSON string and fills the item with the right data.
     *
     * @param json the JSON object in string form.
     */
    private void parseJSON(String json) {
        if (!json.equalsIgnoreCase("")) {
            JSONObject nodeRoot = new JSONObject(json);
            JSONObject nodeName = nodeRoot.getJSONObject("item");
            this.name = nodeName.getString("name");     // the name of the item
            this.image = nodeName.getString("icon");    // the image icon of the item
            nodeName = nodeName.getJSONObject("current");   // now look at the current object
            this.currentPrice = formatPrice(nodeName.get("price").toString());   // look at the current price
            parseAlchData(name);    // Now fill data with data from items.json
        } else {
            System.err.println("ERROR: Couldn't reach OSRS API. ");
        }
    }

    /**
     * Goes through items in the JSON file with the specified item name and fills in
     * the alchemy data.
     *
     * @param name the name of the item to get
     */
    private void parseAlchData(String name) {
        JSONObject nodeRoot = new JSONObject(readFile("src/com/marchsabino/alchr/data/items.json"));
        JSONArray items = nodeRoot.getJSONArray("items");

        JSONObject currentItem;
        for (int i = 0; i < items.length(); i++) {
            currentItem = items.getJSONObject(i);
            if (currentItem.names().get(0).toString().matches("^"+formatName(name)+"$")) {
                this.highAlch = currentItem.getJSONObject(name).getInt("highalch");
                this.lowAlch = currentItem.getJSONObject(name).getInt("lowalch");
                this.buyLimit = currentItem.getJSONObject(name).getInt("buylimit");
                break;
            }
        }
    }

    /**
     * Takes the price of the item in string form and formats it into an
     * integer.  Removing the "k" or "m" at the end and replacing it with zeros.
     *
     * @param price The price of the item.
     * @return The price of the item in integer form.
     */
    private int formatPrice(String price) {
        if (price.charAt(price.length() - 1) == 'k') {
            price = price.substring(0, price.length() - 1);
            double dprice = new Double(price) * 1000;
            return (int) dprice;
        } else if (price.charAt(price.length() - 1) == 'm') {
            price = price.substring(0, price.length() - 1);
            double dprice = new Double(price) * 1000000;
            return (int) dprice;
        } else {
            return new Integer(price.replace(",",""));
        }
    }

    /**
     * Gets the item's id from its name.
     * @param name the name of the item to get its id.
     * @return the id of the item.
     */
    private static int getIdFromName(String name) {
        JSONObject nodeRoot = new JSONObject(readFile("src/com/marchsabino/alchr/data/items.json"));
        JSONArray items = nodeRoot.getJSONArray("items");

        JSONObject currentItem;
        for (int i = 0; i < items.length(); i++) {
            currentItem = items.getJSONObject(i);
            if (currentItem.names().get(0).toString().matches("^"+formatName(name)+"$")) {
                return currentItem.getJSONObject(formatName(name)).getInt("id");
            }
        }

        return 0;
    }

    /**
     * Since case matters, be sure to format it before searching in items.json
     * @param name the name before formatting
     * @return the formatted name, for example "abyssal whip" would format to "Abyssal whip"
     */
    private static String formatName(String name) {
        name = name.toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Reads an entire file and returns the contents of the file in String form.
     * @param fileName the link to the file.
     * @return the contents of the file in string form.
     */
    private static String readFile(String fileName) {
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader(fileName));

            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets the id of this item.
     * @return id of this item.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of this item.
     * @return the name of this item.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the image icon of this item.
     * @return the image icon of this item.
     */
    public String getImage() {
        return image;
    }

    /**
     * Gets the current price of this item.
     * @return the current price of this item.
     */
    public int getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Gets the max quantity you can buy at one time in the GE.
     * @return the maximum quantity you can buy at one time of this item.
     */
    public int getBuyLimit() { return buyLimit; }

    /**
     * Gets the amount of GP you would recieve by high alching this item.
     * This is not the "net profit/loss" this is simply how much you will get.
     * @return the amount of GP you recieve by high alching.
     */
    public int getHighAlch() { return highAlch; }

    /**
     * Gets the amount of GP you would recieve by low alching this item.
     * This is not the "net profit/loss" this is simply how much you will get.
     * @return the amount of GP you recieve by low alching.
     */
    public int getLowAlch() { return lowAlch; }

    /**
     * Is this item a better item to alch than another item.
     * Determines if this item has a better net profit than another item.
     *
     * IF THEY ARE EQUAL IT WILL DEFAULT TO TRUE.
     *
     * @param item the item to compare with.
     * @return true if this item is better than another item, false if the other item is better.
     */
    public boolean betterThan(Item item) {
        return (compareTo(item) >= 0);
    }

    @Override
    public int netProfit(boolean highAlch) {
        return (highAlch)
                ? getHighAlch() - getCurrentPrice() - NATURE_RUNE.getCurrentPrice()
                : getLowAlch() - getCurrentPrice() - NATURE_RUNE.getCurrentPrice();
    }

    /**
     * Compares this item's netProfit to another item's(via HIGH ALCH).
     * Comparing two items will return the item that is more profitable to alch.
     *
     * @param item the item to compare to.
     * @return a negative integer, zero, or a positive integer as this item's net profit is less than, equal to, or greater than the specified item.
     */
    @Override
    public int compareTo(Item item) {
        if (netProfit(true) < item.netProfit(true))
            return -1;
        else if (netProfit(true) == item.netProfit(true))
            return 0;
        else
            return 1;
    }
}
