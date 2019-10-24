package com.intraposition.buzcartsdk.models;

import java.util.List;

public class TriggersData {

    private Trigger[] triggers;

    private float defaultDistance;

    private TriggersMetadata triggersMetadata;

    public TriggersMetadata getTriggersMetadata() {
        return triggersMetadata;
    }

    public void setTriggersMetadata(TriggersMetadata triggersMetadata) {
        this.triggersMetadata = triggersMetadata;
    }

    public float getDefaultDistance() {
        return defaultDistance;
    }

    public void setDefaultDistance(float defaultDistance) {
        this.defaultDistance = defaultDistance;
    }

    public Trigger[] getTriggers() {
        return triggers;
    }

    public void setTriggers(Trigger[] triggers) {
        this.triggers = triggers;
    }
}
