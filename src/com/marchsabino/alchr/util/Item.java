package com.marchsabino.alchr.util;

import com.marchsabino.alchr.net.Connection;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * The Item class.
 * An item is anything and everything that is on the grand exchange.  This includes: runes, armour, potions, bonds, etc.
 *
 * @author Marcello Sabino
 */
public class Item {

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
        } else {
            System.out.println("ERROR: Couldn't reach OSRS API." + Connection.BASE_URL + id);
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

}
