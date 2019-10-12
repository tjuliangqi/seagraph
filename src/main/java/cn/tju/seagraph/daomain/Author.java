package cn.tju.seagraph.daomain;

public class Author {
    private String id;
    private String name;
    private String affiliations;
    private String labels;
    private String paperList;
    private int paperNum;
    private String pic_url;

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

    public String getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(String affiliations) {
        this.affiliations = affiliations;
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

    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", affiliations='" + affiliations + '\'' +
                ", labels='" + labels + '\'' +
                ", paperList='" + paperList + '\'' +
                ", paperNum=" + paperNum +
                ", pic_url='" + pic_url + '\'' +
                '}';
    }
}
