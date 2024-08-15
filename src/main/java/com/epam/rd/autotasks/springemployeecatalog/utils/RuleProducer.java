package com.epam.rd.autotasks.springemployeecatalog.utils;

import java.util.List;
import java.util.function.Consumer;

public class RuleProducer {
    public static List<Consumer<PagingCriteria.PagingRecord>> getDefaultRules() {
        return List.of(
                PagingCriteria
                        .startConfig()
                        .when(r -> r.getSort().equals("lastName"))
                        .then(r -> r.setSort("fullName.lastName"))
                        .build()
        );
    }
}