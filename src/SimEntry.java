import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class SimEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    protected SimDirectory parent;
    private LocalDateTime creationTime;
    private LocalDateTime lastModifiedTime;

    public SimEntry(String name, SimDirectory parent) {
        this.name = name;
        this.parent = parent;
        this.creationTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public SimDirectory getParent() {
        return parent;
    }

    public void setParent(SimDirectory parent) {
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void updateModifiedTime() {
        this.lastModifiedTime = LocalDateTime.now();
    }

    public abstract String getType();
}