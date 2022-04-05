package com.cst2335.teamproject;

/**
 * Previously saved queries are represented through this.
 * @author Kevin Ong
 * @version 1.0
 */
public class SavedQuery {
    String country;
    String from;
    String to;
    long id;

    public SavedQuery(String input, String from, String to, long id) {
        this.country = input;
        this.from = from;
        this.to = to;
        this.id = id;
    }

    public SavedQuery() {
        this.country = "";
        this.from = "";
        this.to = "";
        this.id = 0;
    }

    public String getCountry() {    return country;    }

    public String getFrom() {       return from;       }

    public String getTo() {         return to;         }

    public long getId() {           return id;         }

    public void setCountry(String country) {  this.country = country;   }

    public void setFrom(String from) {        this.from = from;         }

    public void setTo(String to) {            this.to = to;             }

    public void setId(long id) {              this.id = id;             }


    @Override
    public String toString() {

        return String.format("Cases in %s from %s to %s", getCountry(), getFrom(), getTo());
    }

}