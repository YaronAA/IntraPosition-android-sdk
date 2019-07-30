package com.intraposition.buzcartsdk.models;

import java.util.List;

public class TriggersData {

    private List<Trigger> triggers;

    private TriggersMetadata triggersMetadata;

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public TriggersMetadata getTriggersMetadata() {
        return triggersMetadata;
    }

    public void setTriggersMetadata(TriggersMetadata triggersMetadata) {
        this.triggersMetadata = triggersMetadata;
    }
}
