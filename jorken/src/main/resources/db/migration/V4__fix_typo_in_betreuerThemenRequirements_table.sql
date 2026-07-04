drop table betreuer_themen_requirements;

create table betreuer_themen_requirements
(
    betreuer_themen UUID not null references betreuer_themen (id),
    requirement text not null
);