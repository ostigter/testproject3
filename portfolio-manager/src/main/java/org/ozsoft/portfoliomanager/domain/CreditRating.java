// This file is part of the 'portfolio-manager' (Portfolio Manager)
// project, an open source stock portfolio manager application
// written in Java.
//
// Copyright 2015 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

    NA("N/R"),

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
