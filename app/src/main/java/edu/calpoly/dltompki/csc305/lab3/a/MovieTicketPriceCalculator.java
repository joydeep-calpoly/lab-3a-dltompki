package edu.calpoly.dltompki.csc305.lab3.a;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Once configured with the start and end times for matinee pricing and the
 * relevant age thresholds, instances of this class will compute a movie
 * ticket price.
 *
 * <p>Some of the values are hard-coded for purposes of the lab, but should also
 * be configurable.
 */
public class MovieTicketPriceCalculator {
  private static final int STANDARD_PRICE_CENTS = 2700;
  private static final int MATINEE_PRICE_CENTS = 2400;
  private static final int CHILD_DISCOUNT_CENTS = 300;
  private static final int SENIOR_DISCOUNT_CENTS = 400;

  private final LocalTime startMatineeTime;
  private final LocalTime endMatineeTime;
  private final int maxChildAge;
  private final int minSeniorAge;

  /**
   * Configure price calculator.
   *
   * @param startMatineeTime time at which matinee pricing starts (non-null)
   * @param endMatineeTime time at which matinee pricing ends (non-null)
   * @param maxChildAge maximum age for child discount
   * @param minSeniorAge minimum age for senior discount
   */
  public MovieTicketPriceCalculator(final LocalTime startMatineeTime,
                                    final LocalTime endMatineeTime,
                                    final int maxChildAge,
                                    final int minSeniorAge) {
    this.startMatineeTime = Objects.requireNonNull(startMatineeTime,
        "matinee start time cannot be null");
    this.endMatineeTime = Objects.requireNonNull(endMatineeTime,
        "matinee end time cannot be null");
    this.maxChildAge = maxChildAge;
    this.minSeniorAge = minSeniorAge;

    if (startMatineeTime.isAfter(endMatineeTime)) {
      throw new IllegalArgumentException(
          "matinee start time cannot come after end time");
    }
  }

  /**
   * Compute the price of a single ticket for a showing at the specified time
   * and for a customer of the specified age.
   *
   * @param scheduledTime time of the showing
   * @param age attendee age
   * @return return the computed price in cents
   */
  public int computePrice(final LocalTime scheduledTime,
                          final int age) {
    final int discount = computeDiscount(age);

    if ((scheduledTime.equals(startMatineeTime) || scheduledTime.isAfter(startMatineeTime))
        && scheduledTime.isBefore(endMatineeTime)) {
      return MATINEE_PRICE_CENTS - discount;
    } else {
      return STANDARD_PRICE_CENTS - discount;
    }
  }

  public int computeDiscount(final int age) {
    if (age >= minSeniorAge) {
      return SENIOR_DISCOUNT_CENTS;
    } else if (age <= maxChildAge) {
      return CHILD_DISCOUNT_CENTS;
    } else {
      return 0;
    }
  }
}