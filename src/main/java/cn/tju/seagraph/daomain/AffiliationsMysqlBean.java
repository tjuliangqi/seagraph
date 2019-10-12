package cn.tju.seagraph.daomain;

public class AffiliationsMysqlBean {
    private String uuid;
    private String name;
    private String labels;
    private String paperList;
    private String paperNum;
    private String influence;

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

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getPaperList() {
        return paperList;
    }

    public void setPaperList(String paperList) {
        this.paperList = paperList;
    }

    public String getPaperNum() {
        return paperNum;
    }

    public void setPaperNum(String paperNum) {
        this.paperNum = paperNum;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
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
                '}';
    }
}
