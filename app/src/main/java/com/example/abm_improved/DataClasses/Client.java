package com.example.abm_improved.DataClasses;

public class Client {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private int birthdayDate;
    private String uid;
    private boolean manager;

    /**
     * Default constructor for the class
     */
    public Client() {
    }

    /**
     * Constructor  to be called while registering the user. All users registered are not managers
     *
     * @param firstName    first name
     * @param lastName     last name
     * @param email        email
     * @param phoneNumber  phone number
     * @param address      address
     * @param birthdayDate birthdayDate
     * @param uid          uid
     * @param isManager      manager
     */
    public Client(String firstName, String lastName, String email, String phoneNumber, String address, int birthdayDate, String uid, boolean isManager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthdayDate = birthdayDate;
        this.uid = uid;
        this.manager = isManager;
    }

    public Client(String firstName, String lastName, String email, String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public int getBirthdayDate() {
        return birthdayDate;
    }

    public boolean getManager() {
        return manager;
    }

    public String getUid() {
        return uid;
    }
}
