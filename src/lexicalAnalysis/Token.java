package lexicalAnalysis;

public class Token {
    private String words;
    private String seedCode;
    private String attributeValue;
    private int lineNum;

    public Token(String words, String seedCode, String attributeValue,int lineNum) {
        this.words = words;
        this.seedCode = seedCode;
        this.attributeValue = attributeValue;
        this.lineNum = lineNum;
    }

    public String getWords() {
        return this.words;
    }

    public String getSeedCode() {
        return this.seedCode;
    }

    public String getAttributeValue() {
        return this.attributeValue;
    }

    public int getLineNum() {
        return lineNum;
    }
}
