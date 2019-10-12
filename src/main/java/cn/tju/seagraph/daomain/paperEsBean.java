package cn.tju.seagraph.daomain;

import java.util.List;

public class paperEsBean {
    private String id;
    private List authors;
    private List affiliations;
    private String title;
    private String journal;
    private String abs;
    private String pubdate;
    private String type;
    private String browse;
    private List keywords;
    private List chemicallist;
    private String labels;
    private String ch_title;
    private String or_title;
    private String field1;
    private String field2;
    private String field3;
    private String field4;
    private String field5;
    private String field6;

    @Override
    public String toString() {
        return "paperEsBean{" +
                "id='" + id + '\'' +
                ", authors=" + authors +
                ", affiliations=" + affiliations +
                ", title='" + title + '\'' +
                ", journal='" + journal + '\'' +
                ", abs='" + abs + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", type='" + type + '\'' +
                ", browse='" + browse + '\'' +
                ", keywords=" + keywords +
                ", chemicallist=" + chemicallist +
                ", labels='" + labels + '\'' +
                ", ch_title='" + ch_title + '\'' +
                ", or_title='" + or_title + '\'' +
                ", field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                ", field3='" + field3 + '\'' +
                ", field4='" + field4 + '\'' +
                ", field5='" + field5 + '\'' +
                ", field6='" + field6 + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List getAuthors() {
        return authors;
    }

    public void setAuthors(List authors) {
        this.authors = authors;
    }

    public List getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List affiliations) {
        this.affiliations = affiliations;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public List getKeywords() {
        return keywords;
    }

    public void setKeywords(List keywords) {
        this.keywords = keywords;
    }

    public List getChemicallist() {
        return chemicallist;
    }

    public void setChemicallist(List chemicallist) {
        this.chemicallist = chemicallist;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getCh_title() {
        return ch_title;
    }

    public void setCh_title(String ch_title) {
        this.ch_title = ch_title;
    }

    public String getOr_title() {
        return or_title;
    }

    public void setOr_title(String or_title) {
        this.or_title = or_title;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }
}
