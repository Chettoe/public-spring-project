create table betreuer
(
    id UUID primary key,

    first_name text not null,
    last_name  text not null,
    email      text not null,
    phone_number text,

    beschreibung text
);

create table betreuer_tags
(
    tags text,

    betreuer UUID references betreuer (id)
);

create table betreuer_themen
(
    id UUID primary key,
    name text not null,
    markdown text,

    betreuer UUID references betreuer (id)
);

create table betreuer_themen_requirements
(
    requirements text,

    betreuer UUID references betreuer (id)
);