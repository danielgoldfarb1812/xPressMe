package com.example.xpressme;

public class CommunicationBoard {
    String uId;
    String boardName;
    String boardOwnerId;
    BoardButton[] boardButtons;

    public CommunicationBoard(String uId, String boardName, String boardOwnerId){
        this.uId = uId;
        this.boardName = boardName;
        this.boardOwnerId = boardOwnerId;
        this.boardButtons = new BoardButton[18]; // each board has a maximum of 18 buttons
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public BoardButton[] getButtons() {
        return boardButtons;
    }

    public void setButtons(BoardButton[] boardButtons) {
        this.boardButtons = boardButtons;
    }

}
