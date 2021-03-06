CREATE TABLE public.databasechangelog
(
    id            character varying(255) COLLATE pg_catalog."default" NOT NULL,
    author        character varying(255) COLLATE pg_catalog."default" NOT NULL,
    filename      character varying(255) COLLATE pg_catalog."default" NOT NULL,
    dateexecuted  timestamp without time zone                         NOT NULL,
    orderexecuted integer                                             NOT NULL,
    exectype      character varying(10) COLLATE pg_catalog."default"  NOT NULL,
    md5sum        character varying(35) COLLATE pg_catalog."default",
    description   character varying(255) COLLATE pg_catalog."default",
    comments      character varying(255) COLLATE pg_catalog."default",
    tag           character varying(255) COLLATE pg_catalog."default",
    liquibase     character varying(20) COLLATE pg_catalog."default",
    contexts      character varying(255) COLLATE pg_catalog."default",
    labels        character varying(255) COLLATE pg_catalog."default",
    deployment_id character varying(10) COLLATE pg_catalog."default"
)
    TABLESPACE pg_default;

ALTER TABLE public.databasechangelog
    OWNER to admin;

-- Table: public.databasechangeloglock

-- DROP TABLE public.databasechangeloglock;

CREATE TABLE public.databasechangeloglock
(
    id          integer NOT NULL,
    locked      boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby    character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;

ALTER TABLE public.databasechangeloglock
    OWNER to admin;

-- Table: public.user

-- DROP TABLE public."user";

CREATE TABLE public."user"
(
    id            bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    first_name    character varying(255) COLLATE pg_catalog."default" NOT NULL,
    last_name     character varying(255) COLLATE pg_catalog."default" NOT NULL,
    role          character varying(255) COLLATE pg_catalog."default" NOT NULL,
    login         character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password_hash character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT user_login_key UNIQUE (login)
)
    TABLESPACE pg_default;

ALTER TABLE public."user"
    OWNER to admin;

-- Table: public.request

-- DROP TABLE public.request;

CREATE TABLE public.request
(
    id          bigint                                              NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name        character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_id     bigint                                              NOT NULL,
    CONSTRAINT request_pkey PRIMARY KEY (id)
)
    TABLESPACE pg_default;

ALTER TABLE public.request
    OWNER to admin;


INSERT INTO public."user"(id, first_name, last_name, role, login, password_hash)
VALUES (4, 'admin', 'admin', 'ROLE_ADMIN', 'admin', '$2a$10$OWXEElDAhzsESPf52oj9QuXxuYBUpCC4Jd7c0iID9Nn1n3z.jquay'),
       (5, 'user', 'user', 'ROLE_USER', 'user', '$2a$10$LS455NUmOw3WoqVKBUTMQOQbwLkOXTJzDpu6LBWx0VYHaQ7H4km8S');

INSERT INTO public.request(id, name, description, user_id)
VALUES (16, 'request1', 'request1', 4),
       (19, 'test2', 'test2', 5),
       (20, 'request44', 'request44', 5);



