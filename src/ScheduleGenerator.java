import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class ScheduleGenerator {
    private Data data;
    private int cycle = 0;
    private HashMap<String, Integer> tempStock;
    private ArrayList<Process> listOfProcesses = new ArrayList<>();
    public ScheduleGenerator(Data data) {
        this.data = data;
    }

    public int run() {
        tempStock = new HashMap<>(data.getStock());
        for (String need : data.getOptimize()) {
            fillToDoList(need);
        }
        doProcesses();
        return cycle;
    }

    private void doProcesses() {
        ArrayList<Process> doing = new ArrayList<>();
        ArrayList<Integer> startingTime = new ArrayList<>();
        while (!listOfProcesses.isEmpty()) {
//            if (listOfProcesses.size() == 1){
//
//                return;
//            }
            if (!doing.isEmpty()) {
                int minCycle = doing.get(0).getCycle() + startingTime.get(0);
                for(int i = 1; i < doing.size(); i++)
                    if(doing.get(i).getCycle() + startingTime.get(i) < minCycle)
                        minCycle = doing.get(i).getCycle() + startingTime.get(i);

                cycle = minCycle;

                for(int i = 0; i < doing.size(); i++) {
                    if(doing.get(i).getCycle() + startingTime.get(i) == cycle) {
                        data.addItems(doing.get(i).getResults());
                        doing.remove(i);
                        startingTime.remove(i);
                        i--;
                    }
                }
            }

            for(int i = 0; i < listOfProcesses.size(); i++) {
                Process pr = listOfProcesses.get(i);
                if (pr.canDo(data.getStock())) {
                    System.out.println(" " + cycle + ":" + pr.getName());
                    data.removeItems(pr.getNeeds());
                    doing.add(pr);
                    startingTime.add(cycle);
                    listOfProcesses.remove(i);
                    i--;
                }
            }
            if (doing.isEmpty()) {
                return;
            }
        }

        for(int i = 0; i < doing.size(); i++){
            if(doing.get(i).getCycle() + startingTime.get(i) > cycle)
                cycle = doing.get(i).getCycle() + startingTime.get(i);

            data.addItems(doing.get(i).getResults());
        }

    }

    private void fillToDoList(String item) {
        for (Process process : data.getProcesses()) {
            if (process.ContainsItem(item)) {
                planProcess(process);
            }
        }
    }

    private void planItem(String name) {
        for (Process process : data.getProcesses()) {
            if (process.ContainsItem(name)) {
                planProcess(process);
                return;
            }
        }
    }

    private void planProcess(Process process) {
        HashMap<String, Integer> needItems = process.needItems(tempStock);
        for(Map.Entry<String, Integer> entry : needItems.entrySet()) {
            int wasItem = getItem(entry.getKey());
            int temp = getItem(entry.getKey()) -  wasItem;
            while(getItem(entry.getKey()) -  wasItem < entry.getValue()) {
                planItem(entry.getKey());
                if (temp == getItem(entry.getKey()) -  wasItem) {
                    return;
                }
            }

        }
        if (!process.canDo(tempStock)) {
            StockExchangeProgram.ExitProgram(tempStock.toString() + " : " + process.getName());
        }
        removeItems(process.getNeeds());
        addItems(process.getResults());
        listOfProcesses.add(process);
    }

    public void removeItems(HashMap<String, Integer> items) {
        for (Map.Entry<String, Integer> item : items.entrySet()) {
            int quantity = 0;
            if (tempStock.containsKey(item.getKey()))
                quantity = tempStock.get(item.getKey());

            tempStock.put(item.getKey(), quantity - item.getValue());
        }
    }

    public void addItems(HashMap<String, Integer> items) {
        for (Map.Entry<String, Integer> item : items.entrySet()) {
            int quantity = 0;
            if (tempStock.containsKey(item.getKey()))
                quantity = tempStock.get(item.getKey());

            tempStock.put(item.getKey(), quantity + item.getValue());
        }
    }

    private int getItem(String name) {
        for(Map.Entry<String, Integer> entry : tempStock.entrySet())
            if(Objects.equals(name, entry.getKey()))
                return entry.getValue();

        return 0;
    }
}