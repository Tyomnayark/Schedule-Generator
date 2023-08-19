import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Checker {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("you need pass two parameters: <file> <log_file>");
            return;
        }
        Data data = Data.read(args[0]);
        if (data == null) return;

        boolean ok = Check(data, args[1]);
        if (ok) {
            System.out.println("Trace completed, no error detected.");
        } else {
            System.out.println("Exiting...");
        }
    }

    private static boolean Check(Data data, String lofFile) {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(lofFile));
        } catch (Exception e) {
            System.out.println("Cannot access to file");
            return false;
        }
        ArrayList<Process> doing = new ArrayList<>();
        ArrayList<Integer> startingTime = new ArrayList<>();
        int currentCycle = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.trim();

            if (line.isEmpty()) continue;
            if (line.charAt(0) == '#') continue;

            boolean isProcess = Pattern.compile("^\\d+:\\w+$").matcher(line).matches();

            if(isProcess) {
                System.out.println("Evaluating: " + line);
                String[] arr = line.split(":");
                int cycle = Integer.parseInt(arr[0]);
                String processName = arr[1];
                Process process = data.getProcess(processName);
                if (process == null) {
                    System.out.println("Error detected");
                    System.out.println("at " + line + " no such process");
                    return false;
                }
                currentCycle = cycle;
                for(int i = 0; i < doing.size(); i++) {
                    if(doing.get(i).getCycle() + startingTime.get(i) <= cycle) {
                        data.addItems(doing.get(i).getResults());
                        doing.remove(i);
                        startingTime.remove(i);
                        i--;
                    }
                }

                if (!process.canDo(data.getStock())) {
                    System.out.println("Error detected");
                    System.out.println("at " + line + " stock insufficient");
                    return false;
                }
                data.removeItems(process.getNeeds());
                doing.add(process);
                startingTime.add(currentCycle);
            } else {
                System.out.println("Error while parsing `" + line + "`");
                return false;
            }
        }
        return true;
    }
}
