import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {
    private LocalDate date;
    private String category;
    private double amount;
    private String note;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public Expense(LocalDate date, String category, double amount, String note) {
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    public LocalDate getDate() { return date; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getNote() { return note; }

    @Override
    public String toString() {
        return date.format(FMT) + " | " + category + " | ₹" + amount + " | " + note;
    }

    // CSV helpers
    public String toCSV() {
        return date.format(FMT) + "," + escape(category) + "," + amount + "," + escape(note);
    }

    public static Expense fromCSV(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length < 4) return null;
        LocalDate d = LocalDate.parse(parts[0], FMT);
        String cat = unescape(parts[1]);
        double amt = Double.parseDouble(parts[2]);
        String note = unescape(parts[3]);
        return new Expense(d, cat, amt, note);
    }

    private static String escape(String s) {
        return s.replace(",", " ");
    }

    private static String unescape(String s) {
        return s;
    }
}
