package bigbrother.slimdealz.dto;

public class MemberUpdateDTO {

    private String nickname;
    private String card;
    private boolean notification_agree;

    // Getters and Setters

    public boolean getNotification_Agree() {
        return notification_agree;
    }

    public void setNotification_Agree(boolean notification_agree) {
        this.notification_agree = notification_agree;
    }
}