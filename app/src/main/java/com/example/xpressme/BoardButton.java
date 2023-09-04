package com.example.xpressme;

public class BoardButton {
    String buttonLabel;
    String ttsMessage;
    String imageUrl;
    int imgDrawable;
    ButtonAction buttonAction;
    String targetBoard; // move to another board (targetBoard.uId)

    public enum ButtonAction {
        PLAY_MESSAGE,
        MOVE_TO_BOARD
    }

    // a new button consists of id and label. when creating a button we will call the setters
    public BoardButton(String buttonLabel, int imgDrawable){
        this.buttonLabel = buttonLabel;
        this.setImgDrawable(imgDrawable);
    }

    public int getImgDrawable() {
        return imgDrawable;
    }

    public void setImgDrawable(int imgDrawable) {
        this.imgDrawable = imgDrawable;
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
