package com.altaec.jooq.recurse;


import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.altaec.recurse.jooq.sample.model.Tables.DIRECTORY;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.INTEGER;

@Service
public class DirectoryService {
    @Autowired
    DSLContext context;

    /**
     * Метод возвращает все записи в виде иархического дерева, из таблицы DIRECTORY
     */
    public SelectWhereStep<?> getAllRecurseDirectory() {
        var array = array(DIRECTORY.ID);
        var dataTypeId = DIRECTORY.ID.getDataType();
        CommonTableExpression<?> cte = name("t").fields(
                "id",
                "name",
                "path"
        ).as(
                select(
                        DIRECTORY.ID,
                        DIRECTORY.LABEL,
                        array)
                        .from(DIRECTORY)
                        .where(DIRECTORY.PARENT_ID.isNull())
                        .unionAll(
                                select(
                                        DIRECTORY.ID,
                                        DIRECTORY.LABEL,
//                                        arrayAppend(array, field(name("t", "path"),INTEGER)))
                                        function("array_append", array.getDataType(), field(name("t", "path")), DIRECTORY.ID))
//                                        arrayAppend(array, field(name("t", "path"),INTEGER)))
                                        .from(table(name("t")))
                                        .join(DIRECTORY)
                                        .on(field(name("t", "id"), dataTypeId)
                                                .eq(DIRECTORY.PARENT_ID)))
        );

        return context.withRecursive(cte)
                .selectFrom(cte);



    }

    /**
     * Получить иархически построенные данные таблицы, с условием что выбран был элемент searchField, с лимитом;
     * Родительские элементы обязаны возвращаться всегда
     */
    public void getResultWithLimitData() {
        var searchField = "Sub Menu 2";
        System.out.println(getAllRecurseDirectory().where(exists(
                select(inline(1)).from(
                        select()
                                .from(table(quotedName("t")))
                                .where(field(name("t", "name")).eq(inline(searchField)))
                                .limit(inline(1))
                                .asTable("with_limit_table")
                        //todo разобраться с ANY
                ).where(field(name("t", "id")).eq(function("any", INTEGER, field(name("with_limit_table", "path")))))
        )).fetch());
    }


}
