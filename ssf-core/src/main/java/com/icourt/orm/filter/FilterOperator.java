package com.icourt.orm.filter;

/**
 * @author june
 */
public enum FilterOperator {

    eq {
        @Override
        public String toString() {
            return "=";
        }
    },
    ne {
        @Override
        public String toString() {
            return "<>";
        }
    },
    gt {
        @Override
        public String toString() {
            return ">";
        }
    },
    lt {
        @Override
        public String toString() {
            return "<";
        }
    },
    le {
        @Override
        public String toString() {
            return "<=";
        }
    },
    ge {
        @Override
        public String toString() {
            return ">=";
        }
    },
    like, likeStart {
        @Override
        public String toString() {
            return "like*";
        }
    },
    likeEnd {
        @Override
        public String toString() {
            return "*like";
        }
    },
    between, in

}
