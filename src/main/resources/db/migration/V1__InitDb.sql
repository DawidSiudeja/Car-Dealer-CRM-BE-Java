CREATE TABLE "dealer"
(
    id                  serial primary key,
    uuid                varchar not null,
    logo_path           varchar,
    company_name        varchar,
    telephone1          varchar,
    telephone2          varchar,
    telephone3          varchar,
    mail1               varchar,
    mail2               varchar,
    mail3               varchar,
    nip                 varchar not null,
    facebook_link       varchar,
    instagram_link      varchar,
    tiktok_link         varchar,
    otomoto_link        varchar,
    address             varchar,
    desc_html            varchar
);

CREATE TABLE "users"
(
    id                  serial primary key,
    uuid                varchar not null,
    login               varchar not null,
    email               varchar not null,
    password            varchar not null,
    role                varchar not null,
    isLock              boolean DEFAULT true,
    isEnabled           boolean DEFAULT false,
    dealer              integer REFERENCES "dealer" (id)
);

CREATE TABLE "car_for_sale"
(
    id              serial primary key,
    uuid            varchar not null,
    brand           varchar not null,
    model           varchar not null,
    car_year        integer not null,
    color           varchar not null,
    engine          varchar not null,
    mileage         integer not null,
    type            varchar not null,
    gearbox         varchar not null,
    doors           integer not null,
    seats           integer not null,
    desc_html       TEXT not null,
    create_at       DATE,
    seller          integer REFERENCES "dealer" (id),
    price           decimal not null
);

CREATE TABLE "car_for_rent"
(
    id              serial primary key,
    uuid            varchar not null,
    brand           varchar not null,
    model           varchar not null,
    car_year        integer not null,
    color           varchar not null,
    engine          varchar not null,
    mileage         integer not null,
    type            varchar not null,
    gearbox         varchar not null,
    doors           integer not null,
    seats           integer not null,
    desc_html       TEXT not null,
    create_at       DATE,
    seller          integer REFERENCES "dealer" (id),
    price_per_hour  decimal not null,
    price_per_day   decimal not null,
    price_per_week  decimal not null,
    price_per_month decimal not null
);

CREATE TABLE "car_calendar"
(
    id              serial primary key,
    uuid            varchar not null,
    start_rent      DATE,
    end_rent        DATE,
    car_for_rent    integer REFERENCES "car_for_rent" (id)
);
