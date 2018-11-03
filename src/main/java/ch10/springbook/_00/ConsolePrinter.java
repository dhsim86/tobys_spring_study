package ch10.springbook._00;

public class ConsolePrinter implements Printer {

    @Override
    public void print(String message) {
        System.out.print(message);
    }

}
