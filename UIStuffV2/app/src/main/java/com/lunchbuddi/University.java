package com.lunchbuddi;
import java.util.ArrayList;

/**
 * University.java ~ This class holds the basic info of what a university has.
 * @author Emily
 * @date 4/3/2016
 */
public class University
{
    private String name;
    private ArrayList<Person> students;
    private String city;
    private String state;
    // Possible need for String address of the university???

    /**
     * Default Constructor ~
     * Initializes each class variable to be empty.
     */
    public University()
    {
        name = null;
        city = null;
        state = null;
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Sets the university's name.
     */
    public University(String name)
    {
        this.name = name;
        city = null;
        state = null;
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Sets the university's name.
     * @param students ~ Sets the university's students.
     */
    public University(String name, ArrayList<Person> students)
    {
        this.name = name;
        this.students = students;
        city = null;
        state = null;
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Sets the university's name.
     * @param students ~ Sets the university's students.
     * @param city ~ Sets the university's city.
     */
    public University(String name, ArrayList<Person> students, String city)
    {
        this.name = name;
        this.students = students;
        this.city = city;
        state = null;
    }

    /**
     * Initializer Constructor ~
     * @param name ~ Sets the university's name.
     * @param students ~ Sets the university's students.
     * @param city ~ Sets the university's city.
     * @param state ~ Sets the university's state.
     */
    public University(String name, ArrayList<Person> students, String city, String state)
    {
        this.name = name;
        this.students = students;
        this.city = city;
        this.state = state;
    }

    /**
     * Accessor ~
     * @return ~ The university's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Accessor ~
     * @return ~ The university's list of students.
     */
    public ArrayList<Person> getStudents()
    {
        return students;
    }

    /**
     * Accessor ~
     * @return ~ The university's city.
     */
    public String getCity()
    {
        return city;
    }

    /**
     * Accessor ~
     * @return ~ The university's state.
     */
    public String getState()
    {
        return state;
    }

    /**
     * Mutator ~
     * @param name ~ The university's name is set to name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Mutator ~
     * @param students ~ The university's list of students is set to students.
     */
    public void setStudents(ArrayList<Person> students)
    {
        this.students = students;
    }

    /**
     * Mutator ~
     * @param city ~ The university's city is set to city.
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * Mutator ~
     * @param state ~ The university's state is set to state.
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * Adder
     * @param person ~ Adds a student to the university list of students.
     * @return ~ Either true for added or false for not added.
     */
    public boolean addPerson(Person person)
    {
        boolean answer = students.add(person);
        return answer;
    }

    // DAVID WILL WRITE THIS METHOD
    //public ArrayList<Person> findPerson(String name)
   // {
       // boolean answer = students.contains(name);
    //}
}
