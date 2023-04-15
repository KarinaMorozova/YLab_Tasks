package io.ylab.intensive.lesson05.eventsourcing.message;

import io.ylab.intensive.lesson05.eventsourcing.Person;

public class MessageClassContainer {
    private Person person;
    private MessageStatus messageStatus;
    public MessageClassContainer(){}

    public MessageClassContainer(Person person, MessageStatus messageStatus) {
        this.person = person;
        this.messageStatus = messageStatus;
    }

    public Person getPerson() {
        return person;
    }
    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }
}

