package com.horion.tv.define.object;

import java.util.ArrayList;
import java.util.List;

public class Programme extends SkyTvObject {
    private static final long serialVersionUID = 1780268262241637999L;
    public String categoryID = "";
    public String channelID = "";
    public TvTime endTime;
    public transient Channel parentChannel = null;
    public transient TvTime parentTime = null;
    public TvTime startTime;
    public List<OnlineResource> vodOnlineResourceList = new ArrayList();

    public Programme(String id, String name) {
        super(id, name);
    }

    protected void doAfterDeserialize() {
        this.startTime.afterDeserialize();
        this.endTime.afterDeserialize();
    }

    public String getDisplayName() {
        return this.name;
    }
}
