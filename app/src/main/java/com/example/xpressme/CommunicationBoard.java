package com.example.xpressme;

public class CommunicationBoard {
    String uId;
    String boardName;
    String boardOwnerId;
    Button[] buttons;

    public CommunicationBoard(String uId, String boardName, String boardOwnerId){
        this.uId = uId;
        this.boardName = boardName;
        this.boardOwnerId = boardOwnerId;
        this.buttons = new Button[15]; // each board has a maximum of 15 buttons
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

    public Button[] getButtons() {
        return buttons;
    }

    public void setButtons(Button[] buttons) {
        this.buttons = buttons;
    }

}
