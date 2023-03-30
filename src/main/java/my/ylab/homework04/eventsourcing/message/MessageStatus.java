package my.ylab.homework04.eventsourcing.message;

public enum MessageStatus {
    INSERT ("insert"),
    UPDATE ("update"),
    DELETE ("delete");

    private final String title;
    MessageStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
