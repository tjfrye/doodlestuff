package edu.iastate.scribblestuff;

import java.io.Serializable;

public class Game implements Serializable {
    private int numTurns;
    private String partnerName1;
    private String partnerName2;
    private String whoDrawTurn;
    private String currentWord;

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    Game(String partnerName1, String partnerName2, int numTurns, String isDrawTurn, String currentWord) {
        this.partnerName1 = partnerName1;
        this.partnerName2 = partnerName2;
        this.numTurns = numTurns;
        this.whoDrawTurn = isDrawTurn;
        this.currentWord = currentWord;
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
}
