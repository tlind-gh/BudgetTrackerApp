package budgerTrackerProgram;

public class Date {
    //custom date class since I had a hard time figuring out how to parse LocalDate for storing to json
    //year is hard coded to 2024 as this program only handles one calendar year
    final private int year = 2024;
    final private int month;
    final private int day;

    //constructor for date, only allowing valid dates (and throws exception for non-valid dates)
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

    @Override
    public String toString() {
        return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }


    //Static method to get a month in letters for digits (e.g., "January" from 1)
    public static String getMonthAsString(int month) {
        String monthString = null;
        switch (month) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
        }
        return monthString;
    }

}
