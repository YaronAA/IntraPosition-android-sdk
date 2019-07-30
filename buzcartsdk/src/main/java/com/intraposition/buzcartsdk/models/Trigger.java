package com.intraposition.buzcartsdk.models;

public class Trigger {

    private String triggerId;

    private TagLocation[] locations;

    private TriggerMetadata triggerMetadata;

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }

    public TagLocation[] getLocations() {
        return locations;
    }

    public void setLocations(TagLocation[] locations) {
        this.locations = locations;
    }

    public TriggerMetadata getTriggerMetadata() {
        return triggerMetadata;
    }

    public void setTriggerMetadata(TriggerMetadata triggerMetadata) {
        this.triggerMetadata = triggerMetadata;
    }
}
