package ma.ensa.dictionaryapp.model;

import java.io.Serializable;
import java.util.List;

public class Word implements Serializable {
    private String id;
    private String audioFile;
    private List<Definition> definitions;

    public Word() {
    }

    public Word(String id, String audioFile) {
        this.id = id;
        this.audioFile = audioFile;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }



    @Override
    public String toString() {
        return "Word{" +
                "id='" + id + '\'' +
                ", audioFile='" + audioFile + '\'' +
                ", definitions=" + definitions +
                '}';
    }
}
