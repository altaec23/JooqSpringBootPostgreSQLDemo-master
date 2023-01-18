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
