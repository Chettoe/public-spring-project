drop table betreuer_tags;

create table betreuer_tags
(
    betreuer UUID not null references betreuer (id),
    tag text not null
);