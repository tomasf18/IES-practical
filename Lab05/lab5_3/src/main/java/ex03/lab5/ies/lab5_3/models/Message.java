package ex03.lab5.ies.lab5_3.models;

public class Message {
    private String nMec;
    private int generatedNumber;
    private String type;


    public Message() {
    }

    public Message(int nMec, int generatedNumber, String type) {
        this.nMec = String.valueOf(nMec);
        this.generatedNumber = generatedNumber;
        this.type = type;
    }

    // Getters and Setters
    public String getnMec() {
        return nMec;
    }

    public void setnMec(String nMec) {
        this.nMec = nMec;
    }

    public int getGeneratedNumber() {
        return generatedNumber;
    }

    public void setGeneratedNumber(int generatedNumber) {
        this.generatedNumber = generatedNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // toString
    @Override
    public String toString() {
        return "{" +
                "nMec= '" + nMec + '\'' +
                ", generatedNumber= " + generatedNumber +
                ", type= '" + type + '\'' +
                '}';
    }
}