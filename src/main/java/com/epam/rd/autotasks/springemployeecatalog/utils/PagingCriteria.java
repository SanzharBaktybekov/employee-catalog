package com.epam.rd.autotasks.springemployeecatalog.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PagingCriteria {
    public static ConditionPresentation startConfig() {
        return new ConditionPresentation();
    }

    public static Pageable from(PagingRecord pagingRecord) {
        return PageRequest.of(pagingRecord.page, pagingRecord.size, Sort.by(pagingRecord.sort));
    }

    private static Consumer<PagingRecord> of(
            Map<Predicate<PagingRecord>, Consumer<PagingRecord>> map
    ) {
        return pageRecord -> {
            for(var predicate : map.keySet()) {
                if(predicate.test(pageRecord)) {
                    map.get(predicate)
                            .accept(pageRecord);
                }
            }
        };
    }

    public static class ConditionPresentation {
        private final ActionPresentation actionPresentation = new ActionPresentation(this);
        private final Map<Predicate<PagingRecord>, Consumer<PagingRecord>> map = new HashMap<>();
        private void addRule(Predicate<PagingRecord> predicate, Consumer<PagingRecord> consumer) {
            this.map.put(predicate, consumer);
        }
        public ActionPresentation when(Predicate<PagingRecord> predicate) {
            actionPresentation.addPredicate(predicate);
            return actionPresentation;
        }
        public Consumer<PagingRecord> build() {
            return PagingCriteria.of(this.map);
        }
    }

    public static class ActionPresentation {
        private final ConditionPresentation conditionPresentation;
        private Predicate<PagingRecord> predicate;
        private ActionPresentation(ConditionPresentation conditionPresentation) {
            this.conditionPresentation = conditionPresentation;
        }
        public ConditionPresentation then(Consumer<PagingRecord> consumer) {
            this.conditionPresentation.addRule(this.predicate, consumer);
            return this.conditionPresentation;
        }
        private void addPredicate(Predicate<PagingRecord> predicate) {
            this.predicate = predicate;
        }
    }


    public static class PagingRecord {
        private int page;
        private int size;
        private String sort;
        public PagingRecord() {
            this.page = 0;
            this.size = 14;
            this.sort = "lastName";
        }

        public Pageable toPageable() {
            return PageRequest.of(page, size, Sort.by(sort));
        }

        public int getPage() {
            return page;
        }

        public int getSize() {
            return size;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }
}