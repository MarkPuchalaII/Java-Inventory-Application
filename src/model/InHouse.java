package model;

/** Inhouse parts are instances of parts with machine IDs */
public class InHouse extends Part {
    private int machineId;
    /** Initialize an instance of the inHouse class */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** Set the machine ID of the inHouse instance */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }
    /** Get the machine ID of the inHouse instance */
    public int getMachineId() {
        return machineId;
    }
}
