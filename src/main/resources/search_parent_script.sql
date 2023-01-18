/*all_recursive_table - все записи рекурсивного дерева,
  где в path у нас хранится массив id-значений начиная с родителя
  with_limit_table - таблица с определенным кол-вом строк
  with_parent_table - это таблица, в которой помимо самих записей, хранятся родители этих записей (t2.id = any (t3.path))
  */

with recursive "all_recursive_table"("id", "parent_id", "name", "path") as (
    select "we"."directory"."id", parent_id, "we"."directory"."label", ARRAY["we"."directory"."id"]
    from "we"."directory"
    where "we"."directory"."parent_id" is null
    union all
    select "we"."directory"."id",
           "we"."directory".parent_id,
           "we"."directory"."label",
           array_append("all_recursive_table"."path", "we"."directory"."id")
    from "all_recursive_table"
             join "we"."directory"
                  on "all_recursive_table"."id" = "we"."directory"."parent_id"
)
select * from "all_recursive_table" as with_parent_table
where exists (select 1 from
    (select * from "all_recursive_table"
     where "all_recursive_table".name = 'Sub Menu 2'
         fetch next 1 rows only) with_limit_table
              where with_parent_table.id = any (with_limit_table.path)
          )


/*2 версия скрипта, без учёта лимита с родителями*/
with recursive "t"("id", "parent_id", "name", "path") as (
    select "we"."directory"."id", parent_id, "we"."directory"."label", ARRAY["we"."directory"."id"]
from "we"."directory"
where "we"."directory"."parent_id" is null
union all
select "we"."directory"."id",
    "we"."directory".parent_id,
    "we"."directory"."label",
    array_append("t"."path", "we"."directory"."id")
from "t"
join "we"."directory"
on "t"."id" = "we"."directory"."parent_id"
)
select * from "t" as t2
where exists (
    select id from t
where t.name = 'Sub Menu 1'
and t2.id = any (t.path))


