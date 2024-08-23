package bigbrother.slimdealz.dto;

public class MemberUpdateDTO {

    private String nickname;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    private String cardInfo;
    private boolean receiveNotification;
=======
    private String card;
    private boolean notification_agree;
>>>>>>> Stashed changes

    // Getters and Setters

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

<<<<<<< Updated upstream
    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    public boolean isReceiveNotification() {
        return receiveNotification;
    }

    public void setReceiveNotification(boolean receiveNotification) {
        this.receiveNotification = receiveNotification;
=======
    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

=======
    private String card;
    private boolean notification_agree;

    // Getters and Setters

>>>>>>> Stashed changes
    public boolean getNotification_Agree() {
        return notification_agree;
    }

    public void setNotification_Agree(boolean notification_agree) {
        this.notification_agree = notification_agree;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    }
}