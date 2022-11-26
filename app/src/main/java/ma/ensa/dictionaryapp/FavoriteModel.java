package ma.ensa.dictionaryapp;

public class FavoriteModel {
    Integer imageRes;
    String word,wordType;

    public FavoriteModel(Integer imageRes, String word, String wordType) {
        this.imageRes = imageRes;
        this.word = word;
        this.wordType = wordType;
    }

    public Integer getImageRes() {
        return imageRes;
    }

    public void setImageRes(Integer imageRes) {
        this.imageRes = imageRes;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }
}
