package org.ozsoft.portfoliomanager.domain;

public enum CreditRating {

    AAA("AAA"),

    AA_PLUS("AA+"),

    AA("AA"),

    AA_MINUS("AA-"),

    A_PLUS("A+"),

    A("A"),

    A_MINUS("A-"),

    BBB_PLUS("BBB+"),

    BBB("BBB"),

    BBB_MINUS("BBB-"),

    BB_PLUS("BB+"),

    BB("BB"),

    BB_MINUS("BB-"),

    B_PLUS("B+"),

    B("B"),

    B_MINUS("B-"),

    CCC("CCC"),

    CC("CC"),

    C("C"),

    NA("N/A"),

    ;

    private String text;

    CreditRating(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static CreditRating parse(String text) {
        for (CreditRating creditRating : CreditRating.values()) {
            if (creditRating.getText().equals(text)) {
                return creditRating;
            }
        }
        return null;
    }
}
