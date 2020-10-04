package ishift.pl.ComarchBackend.dataModel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CDN.DeklWydr")
public class DeclarationDetails {
    @Id
    @Column(name = "DkW_DkWID")
    private Long id;

    @Column(name = "DkW_DkNID")
    private Long docId;

    @Column(name = "DkW_Rubryka")
    private Integer heading;

    @Column(name = "DkW_Wartosc")
    private String value;

    @Column(name = "DkW_Opis")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
