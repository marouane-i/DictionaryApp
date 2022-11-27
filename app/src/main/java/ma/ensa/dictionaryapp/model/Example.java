package ma.ensa.dictionaryapp.model;

import java.io.Serializable;

public class Example implements Serializable {
    private Long id;
    private String example;

    public Example() {
    }

    public Example(String example) {
        this.example = example;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @Override
    public String toString() {
        return "Example{" +
                "id=" + id +
                ", example='" + example + '\'' +
                '}';
    }
}
