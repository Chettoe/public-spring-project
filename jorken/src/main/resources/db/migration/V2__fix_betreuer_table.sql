drop table if exists betreuer_themen_requirements;

drop table if exists betreuer_tags;

create table betreuer_tags
(
    betreuer UUID not null references betreuer (id),
    tags text not null
);

create table betreuer_themen_requirements
(
    betreuer_themen UUID not null references betreuer_themen (id),
    requirements text not null
)