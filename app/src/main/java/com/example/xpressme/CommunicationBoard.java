package com.example.xpressme;

import java.util.ArrayList;

public class CommunicationBoard {
    String boardId;
    String boardName;
    String boardOwnerId;
    ArrayList<BoardButton> boardButtons;

    // default constructor for presets. no need for owner id yet
    public CommunicationBoard(String boardName, ArrayList<BoardButton> btnArr){

        this.boardName = boardName;
        this.boardButtons = btnArr;
    }
    public CommunicationBoard(String boardName){

        this.boardName = boardName;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardOwnerId() {
        return boardOwnerId;
    }

    public void setBoardOwnerId(String boardOwnerId) {
        this.boardOwnerId = boardOwnerId;
    }

    public ArrayList<BoardButton> getButtons() {
        return boardButtons;
    }

    public void setButtons(ArrayList<BoardButton> boardButtons) {
        this.boardButtons = boardButtons;
    }

}
