package com.example.localarm.contacts;

public class ContactModel {
    private int id;
    private String phoneNo;
    private String name;

    // constructor
    public ContactModel(int id, String name, String phoneNo) {
        this.id = id;
        this.phoneNo = validate(phoneNo);
        this.name = name;
}

private String validate(String phone){
    StringBuilder case1 = new StringBuilder("+91");
    StringBuilder case2 = new StringBuilder("");

    if (phone.charAt(0) != '+') {
        for (int i = 0; i < phone.length(); i++) {
            // remove any spaces or "-"
            if (phone.charAt(i) != '-' && phone.charAt(i) != ' ') {
                case1.append(phone.charAt(i));
            }
        }
        return case1.toString();
    } else {
        for (int i = 0; i < phone.length(); i++) {
            // remove any spaces or "-"
            if (phone.charAt(i) != '-' || phone.charAt(i) != ' ') {
                case2.append(phone.charAt(i));
            }
        }
        return case2.toString();
    }

    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
