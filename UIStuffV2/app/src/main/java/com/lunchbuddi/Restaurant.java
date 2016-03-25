package com.lunchbuddi;

import android.graphics.Point;

/**
 * Created by Emily on 3/25/2016.
 */
public class Restaurant
{
    private String name;
    private String description;
    private Point coordinates;
    private FoodType[] foodTypes;

    /**
     * Default Constructor ~
     * Initializes each class variable to be empty.
     */
    public Restaurant()
    {
        name = null;
        description = null;
        coordinates = null;
        foodTypes = null;
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Set's the restaurant's name.
     */
    public Restaurant(String name)
    {
        this.name = name;
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Set's the restaurant's name.
     * @param description ~ Set's the restaurant's description.
     */
    public Restaurant(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Set's the restaurant's name.
     * @param description ~ Set's the restaurant's description.
     * @param coordinates ~ Set's the restaurant's coordinates.
     */
    public Restaurant(String name, String description, Point coordinates)
    {
        this.name = name;
        this.description = description;
        this.coordinates = coordinates;
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Set's the restaurant's name.
     * @param description ~ Set's the restaurant's description.
     * @param coordinates ~ Set's the restaurant's coordinates.
     * @param foodTypes ~ Set's the restaurant's foodTypes.
     */
    public Restaurant(String name, String description, Point coordinates, FoodType[] foodTypes)
    {
        this.name = name;
        this.description = description;
        this.coordinates = coordinates;
        this.foodTypes = foodTypes;
    }
    // Finish basic accessors/basic mutators/add and remove foodtypes
}
