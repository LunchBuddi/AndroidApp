package com.lunchbuddi;

import android.graphics.Point;

/**
 * Restaurant.java ~ This class holds the basic info of what a restaurant can hold.
 * @author Emily
 * @date 3/25/2016
 */
public class Restaurant
{
    private String name;
    private String description;
    private Point coordinates;
    private FoodType[] foodTypes;
    final static int NUMBER_OF_FOOD_TYPES = 17;

    /**
     * Default Constructor ~
     * Initializes each class variable to be empty.
     */
    public Restaurant()
    {
        name = null;
        description = null;
        coordinates = null;

        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            foodTypes[i] = null;  //sets each element equal to null;
        }
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Set's the restaurant's name.
     */
    public Restaurant(String name)
    {
        this.name = name;
        description = null;
        coordinates = null;

        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            foodTypes[i] = null;  //sets each element equal to null;
        }
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
        coordinates = null;

        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            foodTypes[i] = null;  //sets each element equal to null;
        }
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
        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            foodTypes[i] = null;  //sets each element equal to null;
        }
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

        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            this.foodTypes[i] = foodTypes[i];  //sets each element
        }
    }

    /**
     * Accessor ~
     * @return ~ The restaurant's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Accessor ~
     * @return ~ The restaurant's description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Accessor ~
     * @return ~ The restaurant's coordinates.
     */
    public Point getCoordinates()
    {
        return coordinates;
    }

    /**
     * Accessor ~
     * @return ~ The restaurant's food types.
     */
    public FoodType[] getFoodTypes()
    {
        return foodTypes;
    }

    /**
     * Mutator ~
     * @param name ~ The restaurant's name is set to name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Mutator ~
     * @param description ~ The restaurant's description is set to description.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Mutator ~
     * @param coordinates ~ The restaurant's coordinates is set to coordinates.
     */
    public void setCoordinates(Point coordinates)
    {
        this.coordinates = coordinates;
    }

    /**
     * Mutator ~
     * @param foodTypes ~ The restaurant's food types is set to foodTypes.
     */
    public void setFoodTypes(FoodType foodTypes[])
    {
        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            this.foodTypes[i] = foodTypes[i];  //sets each element
        }
    }

    /**
     * Adds
     * @param oneFoodType ~ Adds another food type to a restaurant object.
     */
    public void addFoodType(FoodType oneFoodType)
    {
        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            if(foodTypes[i] == null)
            {
                foodTypes[i] = oneFoodType;
            }
        }
    }

    /**
     * Removes
     * @param oneFoodType ~ Removes a food type from a restaurant object.
     */
    public void removeFoodType(FoodType oneFoodType)
    {
        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            if (foodTypes[i] == oneFoodType)
            {
                foodTypes[i] = null;
            }
        }
    }

    /**
     * Finds number of food types.
     * @return ~ The number of food types from a restaurant object.
     */
    public int numbOfFoodTypes()
    {
        int size = 0;
        for(int i = 0; i < NUMBER_OF_FOOD_TYPES; i++)
        {
            if(foodTypes[i] != null)
            {
                size++;
            }
        }
        return size;
    }
}
