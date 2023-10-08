package hr.kanezi.postgres.fts.search;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fts_words")
@NoArgsConstructor
@Data
public class FtsWordView {
    @Id
    String word;
    Double similarity;
}
