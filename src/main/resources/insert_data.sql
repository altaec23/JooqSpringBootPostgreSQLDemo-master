create table directory
(
    id        integer not null
        constraint pk_directory
            primary key,
    parent_id integer
        constraint fk_directory
            references directory,
    label     text
);

alter table directory
    owner to we;

INSERT INTO directory (id, parent_id, label)
VALUES
(1, null, 'Menu 1'),
(2, null, 'Menu 2'),
(3, null, 'Menu 3'),
(4, 1, 'Sub Menu 1'),
(5, null, 'Menu 1');



