package com.lunchbuddi;
import java.util.*;

/**
 * Message.java ~ This class holds the basic info of what a message will have.
 * @author Emily
 * @date 3/25/2016
 */
public class Message
{
    private String title;
    private ArrayList<Person> students;
    private Person sender;
    private String contents;

    /**
     * Default Constructor ~
     * Initializes each class variable to be empty.
     */
    public Message()
    {
        title = null;
        students = null;
        sender = null;
        contents = null;
    }

    /**
     * Initializer Constructor ~
     * @param title ~ Sets the message's title.
     */
    public Message(String title)
    {
        this.title = title;
    }

    /**
     * Initializer Constructor ~
     * @param title ~ Sets the message's title.
     * @param contents ~ Sets the message's contents.
     */
    public Message(String title, String contents)
    {
        this.title = title;
        this.contents = contents;
    }

    /**
     * Initializer Constructor ~
     * @param title ~ Sets the message's title.
     * @param contents ~ Sets the message's contents.
     * @param sender ~ Sets the message's sender.
     */
    public Message(String title, String contents, Person sender)
    {
        this.title = title;
        this.contents = contents;
        this.sender = sender;
    }

    /**
     * Initializer Constructor ~
     * @param title ~ Sets the message's title.
     * @param contents ~ Sets the message's contents.
     * @param sender ~ Sets the message's sender.
     * @param students ~ Sets the message's student(s).
     */
    public Message(String title, String contents, Person sender, ArrayList<Person> students)
    {
        this.title = title;
        this.contents = contents;
        this.sender = sender;
        this.students = students;
    }

    /**
     * Accessor ~
     * @return ~ The message's title.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Accessor ~
     * @return ~ The message's contents.
     */
    public String getContents()
    {
        return contents;
    }

    /**
     * Accessor ~
     * @return ~ The message's sender.
     */
    public Person getSender()
    {
        return sender;
    }

    /**
     * Accessor ~
     * @return ~ The message's student(s).
     */
    public ArrayList<Person> getStudents()
    {
        return students;
    }
}
