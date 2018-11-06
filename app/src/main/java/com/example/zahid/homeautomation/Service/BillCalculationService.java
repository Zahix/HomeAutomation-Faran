package com.example.zahid.homeautomation.Service;


import android.util.Log;
import android.widget.Toast;


//Bill Calculator Table for Lesco Residential
//Units     Charges     Discount    fine
// 50+      4.00        -2.00
//100       9.52        -3.73
//101-200   11.32       -3.21
//201-300   12.33       -2.13
//301-700   14.08                  +1.92
//>700      16.04                  +1.96


public class BillCalculationService {

    private int intUnits;
    private double result;

    public BillCalculationService() {
    }

    public boolean validateRequest(String strUnits) {
        boolean validated = false;
        try {
            intUnits = Integer.parseInt(strUnits);
            Log.i("units", String.valueOf(intUnits));
            validated = true;
        } catch (Exception e) {
            Log.e("units", String.valueOf(e));
            intUnits = 0;
        }
        Log.i("validation", "Validated: " + validated + "  Units: " + strUnits);
        return validated;
    }

    public double execute(String StrUnits) {

        intUnits = Integer.parseInt(StrUnits);

        if (intUnits != 0) {
            return calculateBill();
        } else {
            return 0;
        }
    }

    private double calculateBill() {

        if (intUnits != 0) {

            double afterDiscountCharges, secondResult, firstResult, charges, discount, fine;

            if (intUnits > 700) {
                charges = 16.04;
                fine = 1.96;

                int firstUnits = intUnits - 700;
                afterDiscountCharges = charges + fine;
                firstResult = afterDiscountCharges * firstUnits;

                int secondUnits = 700;
                charges = 14.08;
                fine = 1.92;
                afterDiscountCharges = charges + fine;
                secondResult = afterDiscountCharges * secondUnits;

                result = firstResult + secondResult;
            }

            if (intUnits >= 301 && intUnits <= 700) {
                charges = 14.08;
                fine = 1.92;

                int firstUnits = intUnits - 300;
                afterDiscountCharges = charges + fine;
                firstResult = afterDiscountCharges * firstUnits;

                int secondUnits = 300;
                charges = 12.33;
                discount = 2.13;
                afterDiscountCharges = charges - discount;
                secondResult = afterDiscountCharges * secondUnits;

                result = firstResult + secondResult;

            }

            if (intUnits >= 201 && intUnits <= 300) {
                charges = 12.33;
                discount = 2.13;

                int firstUnits = intUnits - 200;
                afterDiscountCharges = charges - discount;
                firstResult = afterDiscountCharges * firstUnits;

                int secondUnits = 200;
                charges = 11.32;
                discount = 3.21;
                afterDiscountCharges = charges - discount;
                secondResult = afterDiscountCharges * secondUnits;

                result = firstResult + secondResult;
            }

            if (intUnits >= 101 && intUnits <= 200) {
                charges = 11.32;
                discount = 3.21;

                int firstUnits = intUnits - 100;
                afterDiscountCharges = charges - discount;
                firstResult = afterDiscountCharges * firstUnits;

                int secondUnits = 100;
                charges = 9.52;
                discount = 3.73;
                afterDiscountCharges = charges - discount;
                secondResult = afterDiscountCharges * secondUnits;

                result = firstResult + secondResult;
            }

            if (intUnits == 100) {
                charges = 9.52;
                discount = 3.73;

                afterDiscountCharges = charges - discount;
                result = afterDiscountCharges * intUnits;

            }

            if (intUnits <= 99) {
                charges = 4.00;
                discount = 2.00;

                afterDiscountCharges = charges - discount;
                result = afterDiscountCharges * intUnits;

            }
        }
        Log.i("result", "Total price: " + result);
        return result;
    }
}
