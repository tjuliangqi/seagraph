package cn.tju.seagraph.daomain;


public class paperMysqlBean {
    private String uuid;
    private String authors;
    private String affiliations;
    private String doi;
    private String title;
    private String journal;
    private String abs;
    private String fulltext;
    private String references;
    private String pubdate;
    private String pdf_url;
    private String type;
    private String pic_url;
    private String pic_text;
    private String keywords;
    private String fulltext_url;
    private String download;
    private String ch_title;
    private String re_uuid;
    private String or_title;
    private String browse;
    private String chemicallist;
    private String labels;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(String affiliations) {
        this.affiliations = affiliations;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
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

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPic_text() {
        return pic_text;
    }

    public void setPic_text(String pic_text) {
        this.pic_text = pic_text;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getFulltext_url() {
        return fulltext_url;
    }

    public void setFulltext_url(String fulltext_url) {
        this.fulltext_url = fulltext_url;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getCh_title() {
        return ch_title;
    }

    public void setCh_title(String ch_title) {
        this.ch_title = ch_title;
    }

    public String getRe_uuid() {
        return re_uuid;
    }

    public void setRe_uuid(String re_uuid) {
        this.re_uuid = re_uuid;
    }

    public String getOr_title() {
        return or_title;
    }

    public void setOr_title(String or_title) {
        this.or_title = or_title;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public String getChemicallist() {
        return chemicallist;
    }

    public void setChemicallist(String chemicallist) {
        this.chemicallist = chemicallist;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "paperMysqlBean{" +
                "uuid='" + uuid + '\'' +
                ", authors=" + authors +
                ", affiliations=" + affiliations +
                ", doi='" + doi + '\'' +
                ", title='" + title + '\'' +
                ", journal='" + journal + '\'' +
                ", abs='" + abs + '\'' +
                ", fulltext='" + fulltext + '\'' +
                ", references=" + references +
                ", pubdate='" + pubdate + '\'' +
                ", pdf_url='" + pdf_url + '\'' +
                ", type='" + type + '\'' +
                ", pic_url=" + pic_url +
                ", pic_text=" + pic_text +
                ", keywords=" + keywords +
                ", fulltext_url='" + fulltext_url + '\'' +
                ", download='" + download + '\'' +
                ", ch_title='" + ch_title + '\'' +
                ", re_uuid=" + re_uuid +
                ", or_title='" + or_title + '\'' +
                ", browse='" + browse + '\'' +
                ", chemicallist=" + chemicallist +
                ", labels='" + labels + '\'' +
                '}';
    }


}
