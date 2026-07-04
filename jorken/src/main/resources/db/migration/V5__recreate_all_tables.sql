drop table if exists betreuer_themen_requirements cascade;
drop table if exists betreuer_themen cascade;
drop table if exists betreuer_tags cascade;
drop table betreuer cascade;


create table betreuer
(
    id serial primary key,
    fach_id UUID,

    first_name text not null,
    last_name  text not null,
    email      text not null,
    phone_number text,
    beschreibung text
);

create table betreuer_tags
(
    betreuer integer not null references betreuer (id) on delete cascade,
    tag text not null
);

create table betreuer_themen
(
    id serial primary key,
    fach_id UUID,

    betreuer integer references betreuer (id) on delete cascade,

    name text not null,
    markdown text
);

create table betreuer_themen_requirements
(
    betreuer_themen integer not null references betreuer_themen (id) on delete cascade,
    requirement text not null
);