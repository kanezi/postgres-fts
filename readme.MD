# PostgreSQL Full Text Search

----

# Table of Contents
1. [Intro](#intro)
2. [Regular Text Search](#regular-text-search)
3. [Full Text Search support](#full-text-search-support)
4. [tsvector](#tsvector)
5. [tsquery](#tsquery)
6. [Operators](#operators)
7. [Indexing](#indexing)
8. [Misspellings](#misspellings)


## Intro

[Youtube video](https://youtu.be/NlFz5_pb7_Q) accompanies the tutorial.

Project is dedicated to showcasing and exploring PostgreSQL database Full Text Search capabilities.

Docker compose file is provided to start the postgres database service and preload movies and quotes data (*init.sql*).

To start the postgres docker container run:
```
docker compose up -d
```
When container starts up, database should be available on localhost:5432 with default db (*fts*), username(*user*) and password(*pass*).

Couple of tables for movies and quotes and related data is installed with initial scripts: 

```
select   *
from     quotes q 

select   *
from     v_quotes vq 


select   *
from     movies m 

select   *
from     v_movies vm 
```


## Regular Text Search

For regular text search postgres provides like, ilike and regular expression operators:

```
select   q.*
from     quotes q
where    1=1
-- like --
and      q.quote like 'Any%'
and      q.quote ~~ 'Any%' 
and      q.quote like '%on_.'
-- ilike --
and      q.quote ilike 'any%'
and      q.quote ~~* 'any%'
-- regexp
and      q.quote ~ '^Any' 
and      q.quote ~* '^any'
```

## Full Text Search Support

For full text search PostgreSQL provides 2 new data types, *tsvector* and *tsquery*.

```
create table drop_me (
  padded_string      char(10)     --  fixed-length, blank padded
 ,non_padded_string  varchar(10)  --  variable-length with limit
 ,unlimited_text     text         --  variable unlimited length => text = varchar without ()
 -- new data types for fts
 ,fts_document       tsvector
 ,fts_query          tsquery
)
```

## tsvector

Tsvector takes care of analyzing text to create documents that represents text in processed form which is furthermore suitable for indexing.

```
select   q.quote
,        q.author
,        to_tsvector(q.quote) 
,        to_tsvector('english', q.quote)
,        to_tsvector('english', q.author)
,        to_tsvector('simple', q.author)
from     quotes q
where    q.id = 1112
```

## tsquery

Tsquery represents a predicate that documents you’re searching on needs to satisfy.

```
select   to_tsquery('world')
,        to_tsquery('world & !light')
--,        to_tsquery('world light')
,        plainto_tsquery('world light')
,        phraseto_tsquery('darkness in the world cannot extinguish the light')
,        websearch_to_tsquery('cook and bake -Captain "nothing or something"') 
```

## Operators

To match tsquery on processed tsvector text **@@** operator is used.

Tsvector supports concatenation operator (**||**) and tsquery logical operators like **||** and **&&**.

```
select   to_tsvector('A nice day for a car ride') 
,        plainto_tsquery('I am riding')
where    1=1
and      to_tsvector('A nice day for a car ride') @@ plainto_tsquery('I am riding')  
and      plainto_tsquery('I am riding bike') @@ to_tsvector('A nice day for a car ride') 
and      to_tsvector('A nice day for a car ride') @@ to_tsquery('ride:*')
and      (
            to_tsvector('A nice day')
         || to_tsvector('for a car ride')
         )
         @@ (
            to_tsquery('ri:*')
         && to_tsquery('day')
         )
```

## Indexing

We can create gin / gist / rum indices on tsvector / tsquery columns.

For simple one table full text search one option is to create additional generated always as column and index it. 

```
alter table quotes 
add column fts_doc tsvector
generated always as (
    setweight(to_tsvector('english', quote), 'A') 
  ||setweight(to_tsvector('simple', author), 'B')
)
stored;


create index on quotes using gin(fts_doc)
```

For multiple tables we can create materialized view and use triggers to refresh the view concurrently when underlying data changes.

```
create materialized view fts_documents
as
select   quotes.*
,        setweight(to_tsvector('simple', quotes.title), 'A') 
||       setweight(to_tsvector(quotes.description), 'A')
||       setweight(to_tsvector(quotes.meta), 'B') fts
from    (
    select   'QUOTES'||'_'||q.id id 
    ,        'QUOTES' type
    ,         q.author title
    ,         q.quote description
    ,         (
      select   coalesce(string_agg(t.tag, ', '), '') AS tags
      from     tags t
      join     quote_tags qt on t.id = qt.tag_id
      where    qt.quote_id = q.id
    ) meta
    ,         '/quotes/'||q.id url
    from      quotes q
) quotes
union all
select   movies.*
,        setweight(to_tsvector('simple', movies.title), 'A') 
||       setweight(to_tsvector('simple', movies.description), 'A')
||       setweight(to_tsvector(movies.meta), 'B') fts
from    (
select   'MOVIES'||'_'||m.id id
,        'MOVIES' type
,        m.title
,        coalesce(( 
           select   string_agg(a.name::text, ', '::text) as string_agg
           from     actors a
           join     movies_actors ma on ma.actor_id = a.id and ma.movie_id = m.id
           ), ''::text) as description
,        coalesce(( 
           select   string_agg(g.name::text, ', '::text) as string_agg
           from     genres g
           join     movies_genres mg on mg.genre_id = g.id and mg.movie_id = m.id
           where    mg.score > 0), ''::text) as meta
,         '/movies/'||m.id url
from     movies m
) movies


create index idx_doc_fts_search on fts_documents using  gin(fts)


  create or replace function refresh_fts_documents()
  returns trigger 
  language plpgsql
  as $$
  begin
      refresh materialized view concurrently fts_documents;
      return null;
  end $$;
 
 
create or replace trigger refresh_fts_4_quotes
  after insert or update or delete or truncate
  on quotes
  for each statement
  execute procedure refresh_fts_documents();



create unique index idx_doc_id on fts_documents (id)
```


## Misspellings

To offer misspelling suggestion to user we can use trigram (**pg_trgm**) extension

It introduces concept of similarity between words and couple of new operators (**<->**, **%**) and functions.

```
create extension pg_trgm

select   show_trgm('Two')
,        show_trgm('Who')
,        similarity('Something', 'something')
,        similarity('Something', 'somthing')
,        similarity('Something', 'unrelated')
,        similarity('Something', 'everything')
,        'Something' % 'somthing' are_similar
,        'Something' <-> 'somthing' how_similar
,        show_limit()
--,        set_limit(0.3)
```

We can create materialized view on a list of words from our documents and offer suggestions based on similarity of what user searches for:

```
create materialized view fts_words
as
select word from  ts_stat(
   'select   to_tsvector(''simple'', fd.title)
    ||       to_tsvector(''simple'', fd.description)
    ||       to_tsvector(''simple'', fd.meta) vect
    from     fts_documents fd'
    )
    
create index words_idx on fts_words using gin(word gin_trgm_ops)

select   v.word
,        similarity(v.word, :q) as similarity
from     fts_words v
where    v.word % :q
order by v.word <-> :q
```