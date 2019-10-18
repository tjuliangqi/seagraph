package cn.tju.seagraph.daomain;

import java.util.Set;

public class AuthorEsBean {
    private String id;
    private String name;
    private Set affiliations;
    private Set labels;
    private String paperList;
    private int paperNum;
    private String pic_url;
    private String paperUUID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(Set affiliations) {
        this.affiliations = affiliations;
    }

    public Set getLabels() {
        return labels;
    }

    public void setLabels(Set labels) {
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

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPaperUUID() {
        return paperUUID;
    }

    public void setPaperUUID(String paperUUID) {
        this.paperUUID = paperUUID;
    }

    @Override
    public String toString() {
        return "AuthorEsBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", affiliations=" + affiliations +
                ", labels=" + labels +
                ", paperList='" + paperList + '\'' +
                ", paperNum=" + paperNum +
                ", pic_url='" + pic_url + '\'' +
                ", paperUUID='" + paperUUID + '\'' +
                '}';
    }
}
