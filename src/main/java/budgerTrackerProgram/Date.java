package budgerTrackerProgram;

public class Date {
    //custom date class since I had a hard time figuring out how to parse LocalDate for storing to json
    //year is hard coded to 2024 as this program only handles one calendar year
    final private int year = 2024;
    final private int month;
    final private int day;

    /*constructor for date, only allowing valid dates (and throws exception for non-valid dates)
    method called by TransactionSystem class*/
    public Date(int month, int day) {
        if (day > 0 && day < 32 && month > 0 && month < 13) {
            switch (month) {
                case 2:
                    if (day > 28) {
                        throw new IllegalArgumentException("Not a valid date");
                    }
                    break;

                case 4, 6, 9, 11:
                    if (day > 30) {
                        throw new IllegalArgumentException("Not a valid date");
                    }
                    break;
            }
        } else {
            throw new IllegalArgumentException("Not a valid date");
        }
        this.month = month;
        this.day = day;
    }

    int getMonth() {
        return month;
    }

    int getDay() {
        return day;
    }

    @Override
    public String toString() {
        return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }

    //Static method to get a month in letters for digits (e.g., "January" from 1)
    public static String getMonthAsString(int month) {
        return switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> null;
        };
    }

}
