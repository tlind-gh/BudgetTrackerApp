package budgerTrackerProgram;

public class Date {
    //custom date class since I had a hard time figuring out how to parse LocalDate for storing to json
    //year is hard coded as this program only handles one calender year
    final private int year = 2024;
    final private int month;
    final private int day;

    //constructor for date, only allowing valid dates for 2024.
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

    public int getMonth() {
        return month;
    }

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

    public int getDay() {
        return day;
    }

}
