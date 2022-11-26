package ma.ensa.dictionaryapp.model;

import java.util.List;

public class Definition {
    private Long id;
    private String definition;
    private String type;
    private List<Example> examples;

    public Definition() {
    }

    public Definition(String definition, String type) {
        this.definition = definition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    @Override
    public String toString() {
        return "Definition{" +
                "id=" + id +
                ", definition='" + definition + '\'' +
                ", type='" + type + '\'' +
                ", examples=" + examples +
                '}';
    }
}
