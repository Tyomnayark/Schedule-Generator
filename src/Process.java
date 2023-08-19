import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Process {
    private String name;
    private HashMap<String, Integer> needs;
    private HashMap<String, Integer> results;
    private int cycle;

    public Process(String name, HashMap<String, Integer> needs, HashMap<String, Integer> results, int cycle) {
        setCycle(cycle);
        setName(name);
        setNeeds(needs);
        setResults(results);
    }
    public boolean ContainsItem(String item) {
        for (Map.Entry<String, Integer> entry : results.entrySet())
            if (Objects.equals(entry.getKey(), item))
                return true;

        return false;
    }
    public boolean canDo(HashMap<String, Integer> stock) {
        for(Map.Entry<String, Integer> entry : needs.entrySet()) {
            int quantity = 0;
            if (stock.containsKey(entry.getKey()))
                quantity = stock.get(entry.getKey());

            if (quantity < entry.getValue())
                return false;
        }
        return true;
    }
    public HashMap<String, Integer> needItems(HashMap<String, Integer> stock) {
        HashMap<String, Integer> needItems = new HashMap<>();
        for(Map.Entry<String, Integer> entry : needs.entrySet()) {
            int quantity = 0;
            if (stock.containsKey(entry.getKey()))
                quantity = stock.get(entry.getKey());

            if (quantity < entry.getValue())
                needItems.put(entry.getKey(), entry.getValue() - quantity);
        }
        return needItems;
    }
    private void setName(String name) {
        this.name = name;
    }
    private void setNeeds(HashMap<String, Integer> needs) {
        this.needs = needs;
    }
    private void setResults(HashMap<String, Integer> results) {
        this.results = results;
    }
    private void setCycle(int cycle) {
        this.cycle = cycle;
    }
    public String getName() {
        return name;
    }
    public HashMap<String, Integer> getNeeds() {
        return needs;
    }
    public HashMap<String, Integer> getResults() {
        return results;
    }
    public int getCycle() {
        return cycle;
    }

    @Override
    public String toString() {
        return "Process:[name:"+getName()+", needs:"+needs.toString()+", results:"+results.toString()+", cycle:"+getCycle()+"]";
    }
}