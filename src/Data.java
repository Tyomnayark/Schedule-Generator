import java.io.File;
import java.util.*;
import java.util.regex.*;

class Data {
    private HashMap<String,Integer> stock = new HashMap<>();
//    private ArrayList<StockItem> stock = new ArrayList<>();
    private final ArrayList<Process> processes = new ArrayList<>();
    private final ArrayList<String> optimize = new ArrayList<>();
    private boolean isTimeOptimize = false;

    public HashMap<String,Integer> getStock() {
        return stock;
    }
    public ArrayList<Process> getProcesses() {
        return processes;
    }
    public ArrayList<String> getOptimize() {
        return optimize;
    }
    public boolean isTimeOptimize() {
        return isTimeOptimize;
    }
    public static Data read(String filePath) {
        try {
			Scanner scanner = new Scanner(new File(filePath));
            Data data = new Data();

			while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.trim();

                if (line.length() == 0) continue;
                if (line.charAt(0) == '#') continue;

                boolean isOpt = Pattern.compile("^optimize:\\(\\w+(;\\w+)*\\)$").matcher(line).matches();
                boolean isStock = Pattern.compile("^\\w+:\\d+$").matcher(line).matches();
                boolean isProcess = Pattern.compile("^\\w+:\\((\\w+:\\d+(;\\w+:\\d+)*)?\\):\\((\\w+:\\d+(;\\w+:\\d+)*)?\\):\\d+$").matcher(line).matches();

                // OPTION
                if (isOpt) {
                    if (!data.optimize.isEmpty())
                        StockExchangeProgram.ExitProgram("Error while parsing `" + line + "`");

                    String[] arr = line.split("\\(")[1].split("\\)")[0].split(";");

                    for (String a : arr) {
                        if (Objects.equals(a, "time")) {
                            data.isTimeOptimize = true;
                            continue;
                        }
                        if (data.optimize.contains(a))
                            StockExchangeProgram.ExitProgram("Error while parsing `" + line + "`");

                        data.optimize.add(a);
                    }
                // STOCK
                } else if (isStock) {
                    String[] options = line.split(":");

                    if (data.stock.containsKey(options[0]))
                        StockExchangeProgram.ExitProgram("Error while parsing `" + line + "`");

                    data.stock.put(options[0], Integer.parseInt(options[1]));
                // Process
                } else if (isProcess) {
                    String name = line.split(":")[0];

                    for (Process pr : data.processes)
                        if (Objects.equals(pr.getName(), name))
                            StockExchangeProgram.ExitProgram("Error while parsing `" + line + "`");

                    line = line.substring(name.length()+2);

                    String needsStr = line.split("\\)")[0];
                    HashMap<String, Integer> needs = getDependencies(needsStr);
                    line = line.substring(needsStr.length()+3);

                    String resultsStr = line.split("\\)")[0];
                    HashMap<String, Integer> results = getDependencies(resultsStr);
                    line = line.substring(resultsStr.length()+2);

                    int cycle = Integer.parseInt(line);

                    Process process = new Process(name, needs, results, cycle);
                    data.processes.add(process);
                } else {
                    StockExchangeProgram.ExitProgram("Error while parsing `" + line + "`");
                }
			}
			scanner.close();
            if (data.processes.size() == 0)
                StockExchangeProgram.ExitProgram("Missing processes");

            if (data.optimize.size() == 0)
                StockExchangeProgram.ExitProgram("Missing what item should optimize");

            return data;
		} catch (Exception e) {
            StockExchangeProgram.ExitProgram("No such file");
		}
        return null;
    }
    private static HashMap<String, Integer> getDependencies(String str) {
        HashMap<String, Integer> dependencies = new HashMap<>();

        if (Objects.equals(str, ""))
            return  dependencies;

        String[] arr = str.split(";");

        for (String dependence : arr) {
            String[] dependenceOptions = dependence.split(":");
            String dependenceName  = dependenceOptions[0];
            int dependenceQuant = Integer.parseInt(dependenceOptions[1]);
            dependencies.put(dependenceName, dependenceQuant);
        }
        return dependencies;
    }
    public Process getProcess(String processName) {
        for(Process process : processes) {
            if (Objects.equals(processName, process.getName())) {
                return process;
            }
        }
        return null;
    }
    public void removeItems(HashMap<String, Integer> items) {
        for (Map.Entry<String, Integer> item : items.entrySet()) {
            int quantity = 0;
            if (stock.containsKey(item.getKey()))
                quantity = stock.get(item.getKey());

            stock.put(item.getKey(), quantity - item.getValue());
        }
    }
    public void addItems(HashMap<String, Integer> items) {
        for (Map.Entry<String, Integer> item : items.entrySet()) {
            int quantity = 0;
            if (stock.containsKey(item.getKey()))
                quantity = stock.get(item.getKey());

            stock.put(item.getKey(), quantity + item.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Stock:\n");
        stock.forEach((k,v) ->{
            res.append(" ").append(k).append(" => ").append(v).append("\n");
        });
        return res.substring(0, res.length()-1); // remove last \n
    }
}