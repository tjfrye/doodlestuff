package edu.iastate.scribblestuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements Serializable {
    private int numTurns;
    private String partnerName1;
    private String partnerName2;
    private String whoDrawTurn;
    private String currentWord;
    private Map<String, Object> pastWords;
    //private ArrayList<String> pastWords = new ArrayList<>();

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    Game(String partnerName1, String partnerName2, int numTurns, String isDrawTurn, String currentWord) {
        this.partnerName1 = partnerName1;
        this.partnerName2 = partnerName2;
        this.numTurns = numTurns;
        this.whoDrawTurn = isDrawTurn;
        this.currentWord = currentWord;
//        pastWords.add("start");
//        pastWords.add("start2");
        pastWords = new HashMap<>();
        pastWords.put("0sgsfgs","start");
        pastWords.put("gsfg1", "start2");
    }

    public int getNumTurns() {
        return numTurns;
    }

    public void setNumTurns(int numTurns) {
        this.numTurns = numTurns;
    }

    public String getPartnerName1() {
        return partnerName1;
    }

    public void setPartnerName1(String partnerName1) {
        this.partnerName1 = partnerName1;
    }

    public String getPartnerName2() {
        return partnerName2;
    }

    public void setPartnerName2(String partnerName2) {
        this.partnerName2 = partnerName2;
    }

    public String getWhoDrawTurn() {
        return whoDrawTurn;
    }

    public void setWhoDrawTurn(String whoDrawTurn) {
        this.whoDrawTurn = whoDrawTurn;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

//    public HashMap<String, String> getPastWords() {
//        return pastWords;
//    }
//
//    public void setPastWords(HashMap<String, String> pastWords) {
//        this.pastWords = pastWords;
//    }


//    public ArrayList<String> getPastWords() {
//        return pastWords;
//    }
//
//    public void setPastWords(ArrayList<String> pastWords) {
//        this.pastWords = pastWords;
//    }


    public Map<String, Object> getPastWords() {
        return pastWords;
    }

    public void setPastWords(Map<String, Object> pastWords) {
        this.pastWords = pastWords;
    }
}
