package hr.kanezi.postgres.fts.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FtsDocumentRepository extends JpaRepository<FtsDocuments, String> {

    @Query(value = """
    select   q.id
    ,        q.type
    ,        q.url
    ,        fts doc
    --,        ts_headline(title, websearch_to_tsquery('simple', :q), 'startSel=<em> stopSel=</em>') as title
    ,        ts_headline(title, websearch_to_tsquery(:q), 'startSel=<em> stopSel=</em>') as title
    ,        ts_headline(description, websearch_to_tsquery('simple', :q), 'startSel=<em> stopSel=</em>') as description
    ,        ts_headline(meta, websearch_to_tsquery(:q), 'startSel=<em> stopSel=</em>') as meta
    ,        greatest(ts_rank_cd(fts, websearch_to_tsquery(:q)), ts_rank_cd(fts, websearch_to_tsquery('simple', :q))) ranking
    from     fts_documents q
    where    fts@@ websearch_to_tsquery(:q) or fts@@ websearch_to_tsquery('simple', :q)
    union all
    -- match beginning of the word id query does not contain string
    select   q.id
    ,        q.type
    ,        q.url
    ,        fts doc
    ,        ts_headline(title, to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as title
    ,        ts_headline(description, to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as description
    ,        ts_headline(meta, to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as meta
    ,        ts_rank_cd(fts, to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*')||':*')) ranking
    from     fts_documents q
    where    fts@@ to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*') ||':*')
    and      :q ~* '^([[:word:]]|[[:digit:]])*$'
    order by ranking desc limit :limit

    """ , nativeQuery = true)
    List<FtsDocuments> ftsSearch(@Param("q") String query, @Param("limit") Long limit);
}
