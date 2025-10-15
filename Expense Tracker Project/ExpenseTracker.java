import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseTracker {
    private static final String FILE = "expenses.csv";
    private static final Scanner sc = new Scanner(System.in);
    private static List<Expense> list = new ArrayList<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) {
        load();
        while (true) {
            System.out.println("\n==== Expense Tracker ====");
            System.out.println("1. Add Expense");
            System.out.println("2. View All");
            System.out.println("3. View By Month (YYYY-MM)");
            System.out.println("4. View By Category");
            System.out.println("5. Monthly Summary (total by category)");
            System.out.println("6. Save & Exit");
            System.out.print("Choice: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": addExpense(); break;
                case "2": viewAll(); break;
                case "3": viewByMonth(); break;
                case "4": viewByCategory(); break;
                case "5": monthlySummary(); break;
                case "6": save(); System.out.println("Saved. Bye!"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void addExpense() {
        try {
            System.out.print("Date (YYYY-MM-DD), leave blank for today: ");
            String d = sc.nextLine().trim();
            LocalDate date = d.isEmpty() ? LocalDate.now() : LocalDate.parse(d, FMT);
            System.out.print("Category (Food, Travel, Bills...): ");
            String cat = sc.nextLine().trim();
            System.out.print("Amount: ");
            double amt = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Note: ");
            String note = sc.nextLine().trim();
            Expense e = new Expense(date, cat, amt, note);
            list.add(e);
            System.out.println("✅ Expense added.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void viewAll() {
        if (list.isEmpty()) { System.out.println("No expenses yet."); return; }
        list.forEach(System.out::println);
    }

    private static void viewByMonth() {
        System.out.print("Enter month (YYYY-MM): ");
        String m = sc.nextLine().trim();
        list.stream()
            .filter(e -> e.getDate().toString().startsWith(m))
            .forEach(System.out::println);
    }

    private static void viewByCategory() {
        System.out.print("Enter category: ");
        String cat = sc.nextLine().trim();
        list.stream()
            .filter(e -> e.getCategory().equalsIgnoreCase(cat))
            .forEach(System.out::println);
    }

    private static void monthlySummary() {
        System.out.print("Enter month (YYYY-MM): ");
        String m = sc.nextLine().trim();
        Map<String, Double> map = new LinkedHashMap<>();
        list.stream()
            .filter(e -> e.getDate().toString().startsWith(m))
            .forEach(e -> map.put(e.getCategory(), map.getOrDefault(e.getCategory(), 0.0) + e.getAmount()));
        if (map.isEmpty()) { System.out.println("No records."); return; }
        System.out.println("Summary for " + m + ":");
        map.forEach((k,v) -> System.out.println(k + " : ₹" + v));
    }

    private static void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                Expense e = Expense.fromCSV(line);
                if (e != null) list.add(e);
            }
        } catch (IOException ex) {
            System.out.println("Failed to load: " + ex.getMessage());
        }
    }

    private static void save() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE))) {
            for (Expense e : list) w.write(e.toCSV() + "\n");
        } catch (IOException ex) {
            System.out.println("Failed to save: " + ex.getMessage());
        }
    }
}
