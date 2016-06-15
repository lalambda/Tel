package com.example.admin.tel;

import java.util.List;
import java.util.Map;

/**
 * Created by yaoyafeng on 16/6/3.
 */
public class Contact {
    private String name;
    private List<Map<String, Integer>> spellAll;
    private String[] spellHead;
    private String mobileNumber;
    private String telephone;
    private String workNumber;
    private String number;
    private int position;
    private boolean isName;

    public Contact(Contact contact) {
        name = contact.getName();
        mobileNumber = contact.getMobileNumber();
        workNumber = contact.getWorkNumber();
        telephone = contact.getTelephone();
        spellAll = contact.getSpellAll();
        spellHead = contact.getSpellHead();
    }
    public Contact() {
    }

    public boolean isName() {
        return isName;
    }

    public void setIsName(boolean isName) {
        this.isName = isName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Integer>> getSpellAll() {
        return spellAll;
    }

    public void setSpellAll(List<Map<String, Integer>> spellAll) {
        this.spellAll = spellAll;
    }

    public String[] getSpellHead() {
        return spellHead;
    }

    public void setSpellHead(String[] spellHead) {
        this.spellHead = spellHead;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + mobileNumber.hashCode() + workNumber.hashCode() + telephone.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Contact))
            return false;
        Contact contact = (Contact) o;
        return contact.getName().equals(name) && contact.getMobileNumber().equals(mobileNumber) &&
                contact.getTelephone().equals(telephone) && contact.getWorkNumber().equals(workNumber);
    }
}
