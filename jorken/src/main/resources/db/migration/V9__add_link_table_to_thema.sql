create table betreuer_themen_links
(
    betreuer_themen integer not null references betreuer_themen (id) on delete cascade,
    link text not null
);