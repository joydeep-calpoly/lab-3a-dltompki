package edu.calpoly.dltompki.csc305.lab3.a;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovieTicketPriceCalculatorTest {
    public static final LocalTime BEFORE_MATINEE_TIME = LocalTime.of(7, 0);
    public static final LocalTime START_MATINEE_TIME = LocalTime.of(10, 0);
    public static final LocalTime IN_MATINEE_TIME = LocalTime.of(11, 0);
    public static final LocalTime END_MATINEE_TIME = LocalTime.of(16, 0);
    public static final LocalTime AFTER_MATINEE_TIME = LocalTime.of(17, 0);
    public static final int MAX_CHILD_AGE = 17;
    public static final int MIN_SENIOR_AGE = 65;
    private MovieTicketPriceCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new MovieTicketPriceCalculator(START_MATINEE_TIME, END_MATINEE_TIME, MAX_CHILD_AGE, MIN_SENIOR_AGE);
    }

    @Nested
    class ComputeDiscount {
        @Test
        void seniorDiscount() {
            int i = calc.computeDiscount(MIN_SENIOR_AGE + 1);
            assertEquals(MovieTicketPriceCalculator.SENIOR_DISCOUNT_CENTS, i);
        }

        @Test
        void childDiscount() {
            int i = calc.computeDiscount(MAX_CHILD_AGE - 1);
            assertEquals(MovieTicketPriceCalculator.CHILD_DISCOUNT_CENTS, i);
        }

        @Test
        void noDiscount() {
            int i = calc.computeDiscount(MAX_CHILD_AGE + 1);
            assertEquals(0, i);
        }
    }

    @Test
    void invalidMatineeTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MovieTicketPriceCalculator(END_MATINEE_TIME, START_MATINEE_TIME, MAX_CHILD_AGE, MIN_SENIOR_AGE);
        });
    }

    @Nested
    class ComputePrice {
        @Nested
        class MatineePrice {
            @Test
            void noDiscount() {
                int i = calc.computePrice(IN_MATINEE_TIME, MAX_CHILD_AGE + 1);
                assertEquals(MovieTicketPriceCalculator.MATINEE_PRICE_CENTS, i);
            }

            @Test
            void childDiscount() {
                int i = calc.computePrice(IN_MATINEE_TIME, MAX_CHILD_AGE - 1);
                assertEquals(
                        MovieTicketPriceCalculator.MATINEE_PRICE_CENTS - MovieTicketPriceCalculator.CHILD_DISCOUNT_CENTS,
                        i);
            }

            @Test
            void seniorDiscount() {
                int i = calc.computePrice(IN_MATINEE_TIME, MIN_SENIOR_AGE + 1);
                assertEquals(
                        MovieTicketPriceCalculator.MATINEE_PRICE_CENTS - MovieTicketPriceCalculator.SENIOR_DISCOUNT_CENTS,
                        i);
            }

            @Test
            void atStartTime() {
                int i = calc.computePrice(START_MATINEE_TIME, MIN_SENIOR_AGE - 1);
                assertEquals(MovieTicketPriceCalculator.MATINEE_PRICE_CENTS, i);
            }
        }

        @Nested
        class StandardPrice {
            @Test
            void noDiscount() {
                int i = calc.computePrice(AFTER_MATINEE_TIME, MIN_SENIOR_AGE - 1);
                assertEquals(MovieTicketPriceCalculator.STANDARD_PRICE_CENTS, i);
            }

            @Test
            void childDiscount() {
                int i = calc.computePrice(AFTER_MATINEE_TIME, MAX_CHILD_AGE - 1);
                assertEquals(
                        MovieTicketPriceCalculator.STANDARD_PRICE_CENTS - MovieTicketPriceCalculator.CHILD_DISCOUNT_CENTS,
                        i);
            }

            @Test
            void seniorDiscount() {
                int i = calc.computePrice(AFTER_MATINEE_TIME, MIN_SENIOR_AGE + 1);
                assertEquals(
                        MovieTicketPriceCalculator.STANDARD_PRICE_CENTS - MovieTicketPriceCalculator.SENIOR_DISCOUNT_CENTS,
                        i);
            }

            @Test
            void before() {
                int i = calc.computePrice(BEFORE_MATINEE_TIME, MIN_SENIOR_AGE - 1);
                assertEquals(MovieTicketPriceCalculator.STANDARD_PRICE_CENTS, i);
            }
        }
    }
}