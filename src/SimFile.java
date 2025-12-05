import java.io.Serializable;

public class SimFile implements Serializable {
    private String name;
    private String content;

    public SimFile(String name) {
        this.name = name;
        this.content = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}