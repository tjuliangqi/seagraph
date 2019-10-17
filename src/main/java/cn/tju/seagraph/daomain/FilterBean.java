package cn.tju.seagraph.daomain;

import java.util.List;
import java.util.Set;

public class FilterBean {
    private List<String> pubdate;
    private Set<String> journal;
    private Set<String> affiliations;
    private Set<String> labels;

    public List<String> getPubdate() {
        return pubdate;
    }

    public void setPubdate(List<String> pubdate) {
        this.pubdate = pubdate;
    }

    public Set<String> getJournal() {
        return journal;
    }

    public void setJournal(Set<String> journal) {
        this.journal = journal;
    }

    public Set<String> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(Set<String> affiliations) {
        this.affiliations = affiliations;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "FilterBean{" +
                "pubdate=" + pubdate +
                ", journal=" + journal +
                ", affiliations=" + affiliations +
                ", labels=" + labels +
                '}';
    }
}
