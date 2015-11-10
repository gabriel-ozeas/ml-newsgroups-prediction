package knoma.newsgroup;

/**
 * Created by gabriel on 03/11/15.
 */
public class Group {
    private String name;
    private String path;

    public Group(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
