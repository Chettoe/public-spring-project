create table betreuer_links
(
    betreuer integer not null references betreuer (id) on delete cascade,
    link text not null
);