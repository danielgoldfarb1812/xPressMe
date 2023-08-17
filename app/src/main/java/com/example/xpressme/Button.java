package com.example.xpressme;

public class Button {
    String uId;
    String buttonLabel;
    String ttsMessage;
    String imageUrl;
    ButtonAction buttonAction;
    String targetBoard; // move to another board (targetBoard.uId)

    public enum ButtonAction {
        PLAY_MESSAGE,
        MOVE_TO_BOARD
    }

    // a new button consists of id and label. when creating a button we will call the setters
    public Button(String uId, String buttonLabel){
        this.uId = uId;
        this.buttonLabel = buttonLabel;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public String getTtsMessage() {
        return ttsMessage;
    }

    public void setTtsMessage(String ttsMessage) {
        this.ttsMessage = ttsMessage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ButtonAction getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(ButtonAction buttonAction) {
        this.buttonAction = buttonAction;
    }

    public String getTargetBoard() {
        return targetBoard;
    }

    public void setTargetBoard(String targetBoard) {
        this.targetBoard = targetBoard;
    }
}
