package com.altaec.jooq.recurse;


import org.jooq.CommonTableExpression;
import org.jooq.DSLContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import static com.altaec.recurse.jooq.sample.model.Tables.*;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;


@Service
public class DirectoryService {
    @Autowired
    DSLContext context;

    public void getAllRecurseDirectory() {
        CommonTableExpression<?> cte = name("t").fields(
                "id",
                "name",
                "path"
        ).as(
                select(
                        DIRECTORY.ID,
                        DIRECTORY.LABEL,
                        DIRECTORY.LABEL)
                        .from(DIRECTORY)
                        .where(DIRECTORY.PARENT_ID.isNull())
                        .unionAll(
                                select(
                                        DIRECTORY.ID,
                                        DIRECTORY.LABEL,
                                        field(name("t", "path"), VARCHAR)
                                                .concat("\\")
                                                .concat(DIRECTORY.LABEL))
                                        .from(table(name("t")))
                                        .join(DIRECTORY)
                                        .on(field(name("t", "id"), INTEGER)
                                                .eq(DIRECTORY.PARENT_ID)))
        );

        System.out.println(
                context.withRecursive(cte)
                        .selectFrom(cte)
                        .fetch()
        );

    }


}
