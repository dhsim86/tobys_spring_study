package ch10.springbook._00;

public class StringPrinter implements Printer {

    private StringBuffer buffer = new StringBuffer();

    @Override
    public void print(String message) {
        this.buffer.append(message);
    }

    public String toString() {
        return this.buffer.toString();
    }

}
