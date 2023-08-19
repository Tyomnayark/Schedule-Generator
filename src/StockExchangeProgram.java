import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;

public class StockExchangeProgram {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("you need pass two parameters: <file> <waiting_time>");
            return;
        }
        double t = 1;
        try {
            t = Float.parseFloat(args[1]);
        } catch (Exception e) {
            System.out.println("Write ine value at second parameter");
            return;
        }

        long endTime = (long) (System.currentTimeMillis() + t * 1000);

        Data data = Data.read(args[0]);
        if (data == null) return;

        try (FileOutputStream fileOutputStream = new FileOutputStream(args[0] + ".log");
             PrintStream filePrintStream = new PrintStream(fileOutputStream);
             FilePrefixOutputStream filePrefixStream = new FilePrefixOutputStream(filePrintStream, false)){

            filePrefixStream.enablePrefix();

            PrintStream consolePrintStream = System.out;

            ScheduleGenerator generator = new ScheduleGenerator(data);
            int cycle = 0;

            MultiOutputStream multiOutputStream = new MultiOutputStream(consolePrintStream, filePrefixStream);
            PrintStream multiPrintStream = new PrintStream(multiOutputStream);
            System.setOut(multiPrintStream);

            System.out.println("Main Processes:");
            filePrefixStream.disablePrefix();
            if (data.isTimeOptimize()) {
                cycle = generator.run();
            } else {
                while (System.currentTimeMillis() <= endTime)
                    cycle = generator.run();
            }
            filePrefixStream.enablePrefix();
            System.out.println("No more processes doable at cycle " + (cycle + 1));
            System.out.println(data);

            multiPrintStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void ExitProgram(String error) {
        System.out.println(error);
        ExitProgram();
    }

    public static void ExitProgram() {
        System.out.println("Exiting...");
        System.exit(0);
    }
}
