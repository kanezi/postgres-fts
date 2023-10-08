package hr.kanezi.postgres.fts.search;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fts_documents")
@Data
@NoArgsConstructor
public class FtsDocuments {
    @Id
    String id;
    String type;
    String title;
    String description;
    String meta;
    String url;
    String doc;

    //@Transient
    Double ranking;
}
