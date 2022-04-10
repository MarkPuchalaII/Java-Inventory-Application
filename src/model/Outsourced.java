package model;

/** Outsourced parts are instances of parts with associated company names */
public class Outsourced extends Part {
    private String companyName;

    /** Initialize an instance of the Outsourced class */
    public Outsourced (int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** Set the current company name */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /** Get the current company name */
    public String getCompanyName() {
        return companyName;
    }
}
