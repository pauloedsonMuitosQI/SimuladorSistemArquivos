import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class SimDirectory implements Serializable {
    private String name;
    private Map<String, SimFile> files = new HashMap<>();
    private Map<String, SimDirectory> directories = new HashMap<>();

    public SimDirectory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public Map<String, SimFile> getFiles() {
        return files;
    }

    public Map<String, SimDirectory> getDirectories() {
        return directories;
    }
}