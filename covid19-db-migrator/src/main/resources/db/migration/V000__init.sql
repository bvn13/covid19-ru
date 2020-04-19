--create schema covid;

create sequence covid.cvd_regions_seq start 1 increment by 1 no minvalue no maxvalue cache 1;

create table covid.regions
(
    id bigint not null default nextval('covid.cvd_regions_seq')
        constraint regions_pkey primary key,
    name varchar(255) not null
        constraint regions_name_uniq unique
);

alter table covid.regions owner to covid19;
alter sequence covid.cvd_regions_seq owner to covid19;
alter sequence covid.cvd_regions_seq owned by covid.regions.id;

create sequence covid.cvd_updates_seq start 1 increment by 1 no minvalue no maxvalue cache 1;

create table covid.cvd_updates
(
    id bigint not null default nextval('covid.cvd_updates_seq')
        constraint cvd_updates_pkey primary key,
    created_on timestamp
        constraint cvd_upd_created_uniq unique,
    datetime timestamp
);

alter table covid.cvd_updates owner to covid19;
alter sequence covid.cvd_updates_seq owner to covid19;
alter sequence covid.cvd_updates_seq owned by covid.cvd_updates.id;

create sequence covid.cvd_stats_seq start 1 increment by 1 no minvalue no maxvalue cache 1;

create table if not exists covid.cvd_stats
(
	id bigint not null default nextval('covid.cvd_stats_seq')
		constraint cvd_stats_pkey primary key,
	created_on timestamp,
	died bigint not null,
	healed bigint not null,
	sick bigint not null,
	region_id bigint
		constraint fk_cvd_stats_region references covid.regions,
	update_id bigint
		constraint fk_cvd_stats_updates references covid.cvd_updates
);

alter table covid.cvd_stats owner to covid19;
alter sequence covid.cvd_stats_seq owner to covid19;
alter sequence covid.cvd_stats_seq owned by covid.cvd_stats.id;

