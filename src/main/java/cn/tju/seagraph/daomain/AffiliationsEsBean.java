package cn.tju.seagraph.daomain;

import java.util.ArrayList;
import java.util.Set;

public class AffiliationsEsBean {
    private String uuid;
    private String name;
    private Set<String> labels;
    private String paperList;
    private int paperNum;
    private String influence;
    private String paperUUID;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    public String getPaperList() {
        return paperList;
    }

    public void setPaperList(String paperList) {
        this.paperList = paperList;
    }

    public int getPaperNum() {
        return paperNum;
    }

    public void setPaperNum(int paperNum) {
        this.paperNum = paperNum;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public String getPaperUUID() {
        return paperUUID;
    }

    public void setPaperUUID(String paperUUID) {
        this.paperUUID = paperUUID;
    }
    @Override
    public String toString() {
        return "affiliationsMysqlBean{" +
                "uuid='" + uuid + '\'' +
                ", name=" + name +
                ", labels=" + labels +
                ", paperList='" + paperList + '\'' +
                ", paperNum='" + paperNum + '\'' +
                ", influence='" + influence + '\'' +
                ", paperUUID='" + paperUUID + '\'' +
                '}';
    }

}
