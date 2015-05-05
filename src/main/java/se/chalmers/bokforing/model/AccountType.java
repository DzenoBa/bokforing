/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.chalmers.bokforing.model;

/**
 * Represents account types in the BAS-kontoplan
 *
 * @see http://sv.wikipedia.org/wiki/BAS-kontoplan
 *
 * @author Jakob
 */
public enum AccountType {

    ASSETS(1),
    FUNDS_AND_DEBT(2),
    REVENUE(3),
    MATERIAL_AND_PRODUCT_COSTS(4),
    COSTS_5(5),
    COSTS_6(6),
    COSTS_7(7),
    COSTS_8(8);

    private final int startingDigit;

    AccountType(int startingDigit) {
        this.startingDigit = startingDigit;
    }

    public int getStartingDigit() {
        return this.startingDigit;
    }

}
