package com.cs407_android.ormlab;

/**
 * Created by njaunich on 2/18/16.
 */
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class ExampleDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.cs407_android.ormlab"); //Scheme for GreenDAO ORM
        createDB(schema);
        new DaoGenerator().generateAll(schema, "./app/src/main/java/");
        //where you want to store the generated classes.
    }

    private static void createDB(Schema schema) {

        //Add Event
        Entity event = schema.addEntity("Event");
        event.addIdProperty();
        event.addStringProperty("date");
        event.addStringProperty("details");
        event.addBooleanProperty("display");

        /* //guestList table
        Entity guestList = schema.addEntity("GuestList");
        guestList.setTableName("GuestList");
        guestList.addIdProperty();

        Property addDate = guestList.addDateProperty("date").getProperty();
        Property guestId = guestList.addLongProperty("guestId").notNull().getProperty();

        //Add one-to-one relationship between guestList and the guest entity
        guestList.addToOne(guest, guestId);

        //Add one-to-many relationship between guest and guestList
        ToMany guestToGuestList = guestList.addToMany(guest, guestId);

        //Set name of Entity
        guestToGuestList.setName("GuestList"); */


    }

}