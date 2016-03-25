package com.lunchbuddi;
import java.util.*;

/**
 * Person.java ~ This class holds the basic info of what a user has.
 * @author Emily
 * @date 3/25/2016
 */
public class Person
{
    private String name;
    private String password;
    private String email;
    private String major;
    private int gradYear;
    private String hometown;
    private ArrayList<String> interests;
    private ArrayList<String> currentClubs;
    private ArrayList<String> favPlacesToEat;
    boolean bringsLunch;
    // Still need to have class variables for availability for all days of week
    // https://www.mymajors.com/college-majors/

    /**
     * Default Constructor ~
     * Initializes each class variable to be empty.
     */
    public Person()
    {
        name = null;
        password = null;
        email = null;
        major = null;
        gradYear = 0;
        hometown = null;
        interests = null;
        currentClubs = null;
        favPlacesToEat = null;
        bringsLunch = false;  // Starts off with initial value of false
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Sets the person's name.
     * @param password ~ Sets the person's password.
     * @param email ~ Sets the person's email.
     * @param major ~ Sets the person's major.
     * @param gradYear ~ Sets the person's gradYear.
     * @param hometown ~ Sets the person's hometown.
     */
    public Person(String name, String password, String email, String major, int gradYear, String hometown, ArrayList<String> interests, ArrayList<String> currentClubs, ArrayList<String> favPlacesToEat, boolean bringsLunch)
    {
        this.name = name;
        this.password = password;
        this.email = email;
        this.major = major;
        this.gradYear = gradYear;
        this.hometown = hometown;
        this.interests = interests;
        this.currentClubs = currentClubs;
        this.favPlacesToEat = favPlacesToEat;
        this.bringsLunch = bringsLunch;
    }

    /**
     * Accessor ~
     * @return ~ The person's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Accessor ~
     * @return ~ The person's password.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Accessor ~
     * @return ~ The person's email.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Accessor ~
     * @return ~ The person's major.
     */
    public String getMajor()
    {
        return major;
    }

    /**
     * Accessor ~
     * @return ~ The person's gradYear.
     */
    public int getGradYear()
    {
        return gradYear;
    }

    /**
     * Accessor ~
     * @return ~ The person's hometown.
     */
    public String getHometown()
    {
        return hometown;
    }

    /**
     * Accessor ~
     * @return ~ The person's interests.
     */
    public ArrayList<String> getInterests()
    {
        return interests;
    }

    /**
     * Accessor ~
     * @return ~ The person's currentClubs.
     */
    public ArrayList<String> currentClubs()
    {
        return currentClubs;
    }

    /**
     * Accessor ~
     * @return ~ The person's favPlacesToEat.
     */
    public ArrayList<String> favPlacesToEat()
    {
        return favPlacesToEat;
    }

    /**
     * Accessor ~
     * @return ~ The person's decision to bringLunch
     */
    public boolean getBringsLunch()
    {
        return bringsLunch;
    }

    /**
     * Mutator ~
     * @param name ~ The person's name is set to name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Mutator ~
     * @param password ~ The person's password is set to password.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Mutator ~
     * @param email ~ The person's email is set to email.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Mutator ~
     * @param major ~ The person's major is set to major.
     */
    public void setMajor(String major)
    {
        this.major = major;
    }

    /**
     * Mutator ~
     * @param gradYear ~ The person's gradYear is set to gradYear.
     */
    public void setGradYear(int gradYear)
    {
        this.gradYear = gradYear;
    }

    /**
     * Mutator ~
     * @param hometown ~ The person's hometown is set to hometown.
     */
    public void setHometown(String hometown)
    {
        this.hometown = hometown;
    }

    /**
     * Mutator ~
     * @param interests ~ The person's interests is set to interests.
     */
    public void setInterests(ArrayList<String> interests)
    {
        this.interests = interests;
    }

    /**
     * Mutator ~
     * @param currentClubs ~ The person's currentClubs is set to currentClubs.
     */
    public void setCurrentClubs(ArrayList<String> currentClubs)
    {
        this.currentClubs = currentClubs;
    }

    /**
     * Mutator ~
     * @param favPlacesToEat ~ The person's favPlacesToEat is set to favPlacesToEat.
     */
    public void setFavPlacesToEat(ArrayList<String> favPlacesToEat)
    {
        this.favPlacesToEat = favPlacesToEat;
    }

    /**
     * Mutator ~
     * @param bringsLunch ~ The person's option to bringLunch is set to bringsLunch.
     */
    public void setBringsLunch(boolean bringsLunch)
    {
        this.bringsLunch = bringsLunch;
    }
}
