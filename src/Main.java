import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

class Discount {
    private int ID;
    private int percentage;
    private Order order;

    public Discount(int ID, int percentage) {
        this.ID = ID;
        this.percentage = percentage;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getPercentage() {
        return percentage;
    }
}

enum Status { PENDING, SUBMITTED }

class Order {

    private int ID;
    private Customer customer;
    private Status status = Status.PENDING;

    public Order(int ID, Customer customer) {
        this.ID = ID;
        this.customer = customer;
    }

    public int getID() {
        return ID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addItem (Good good, int amount) {}

    public void removeItem (Good good) {}

    public HashMap<Good, Integer> getItems() { return null; }

    public int calculatePrice() { return 0; }

    public void addDiscount(Discount discount) {}

}

class Repository implements Comparable<Repository> {
    private int ID;
    private int capacity;
    private HashMap<Good,Integer> goods = new HashMap<>();

    public Repository(int ID, int capacity) {
        this.ID = ID;
        this.capacity = capacity;
    }

    public int getID() {
        return ID;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFreeCapacity() {
        int count = 0;
        for(Integer i : goods.values()) {
            count += i;
        }
        return capacity - count;
    }

    public HashMap<Good, Integer> getGoods() {
        return goods;
    }

    public void addGood(Good g, int amount) {
        if (amount <= getFreeCapacity()) {
            int newAmount = goods.getOrDefault(g, 0) + amount ;
            goods.put(g, newAmount);
        }
    }

    public void removeGood(Good g, int amount) {
        goods.put(g, Math.min(goods.getOrDefault(g, 0) - amount, 0));
    }

    @Override
    public int compareTo(Repository r) {
        return getCapacity() - r.getCapacity();
    }

    @Override
    public String toString() {
        return "Repository{" +
                "ID=" + ID +
                ", capacity=" + capacity +
                ", freeCapacity=" + getFreeCapacity() +
                ", goods=" + goods +
                '}';
    }

    public String report() {
        return ID +
                "," + capacity +
                "," + getFreeCapacity();
    }
}

class Good {
    private String name;
    private int ID;
    private int price;

    public Good(String name, int ID, int price) {
        this.name = name;
        this.ID = ID;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public int getPrice() {
        return price;
    }
}

class Customer {
    private String name;
    private int ID;
    private int balance;

    public Customer(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void addOrder(Order order) {}

    public Order[] getTotalOrders() { return null; }

    public Order[] getPendingOrders() { return null; }

    public Order[] getSubmittedOrders() { return null; }

    public void submitOder(Order order) {}
}

class Shop {
    private String name;
    private int income;
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Repository> repositories = new ArrayList<>();
    private ArrayList<Good> goods = new ArrayList<>();
    private ArrayList<Discount> discounts = new ArrayList<>();
    private HashMap<Good, Integer> itemsSold = new HashMap<>();

    public Shop(String name) {
        this.name = name;
    }

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public Customer[] getCustomers() {
        Customer[] custs = new Customer[customers.size()];
        return customers.toArray(custs);
    }

    public void addRepository(Repository r) {
        repositories.add(r);
    }

    public Repository[] getRepositories() {
        Repository[] repos = new Repository[repositories.size()];
        return repositories.toArray(repos);
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void addGood(Good g) {
        goods.add(g);
    }

    public void increamentGood(Good g, int amount) {
        Collections.sort(repositories);
        for (Repository repo : repositories) {
            if (repo.getFreeCapacity() >= amount) {
                repo.addGood(g, amount);
                break;
            }
        }
    }

    public Good[] getGoods() {
        Good[] g = new Good[goods.size()];
        return goods.toArray(g);
    }

    public void addDiscount(Discount d, Customer c) {}

    public HashMap<Good, Integer> getItemsSold() {
        return itemsSold;
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", income=" + income +
                ", customers=" + customers +
                ", repositories=" + repositories +
                ", goods=" + goods +
                '}';
    }
}

public class Main {


    private static Scanner sc = new Scanner(System.in);
    private static Shop shop = new Shop("Radin's Shop");

    public static void main(String[] args) {


        while (true) {
            String command = sc.next();
            if (command.equals("add")) addCommand(sc.next());
            if (command.equals("report")) reportCommand(sc.next());
            if (command.equals("remove")) removeCommand(sc.next());
            if (command.equals("submit")) submitCommand(sc.next());
            if (command.equals("terminate")) break;
        }
    }

    private static void addCommand(String command) {
        if (command.equals("customer")) {
            int ID = sc.nextInt();
            String name = sc.next();
            shop.addCustomer(new Customer(name, ID));
        }
        if (command.equals("good")) {
            int ID = sc.nextInt();
            String name = sc.next();
            int price = sc.nextInt();
            int amount = sc.nextInt();
            shop.increamentGood(new Good(name, ID, price), amount);
        }
        if (command.equals("repository")) {
            int ID = sc.nextInt();
            int capacity = sc.nextInt();
            shop.addRepository(new Repository(ID, capacity));
        }
        if (command.equals("balance")) ;
        if (command.equals("item")) ;
        if (command.equals("discount")) {
            int ID = sc.nextInt();
            int percentage = sc.nextInt();
            shop.addDiscount(new Discount(ID, percentage));
        }
    }

    private static void reportCommand(String command) {
        if (command.equals("customers")) ;
        if (command.equals("repositories")) {
            for (Repository r : shop.getRepositories()) {
                System.out.println(r.report());
            }
        }
        if (command.equals("income")) {
            System.out.println(shop.getIncome());
        }
    }

    private static void removeCommand(String command) {
        if (command.equals("item")) ;
    }

    private static void submitCommand(String command) {
        if (command.equals("order")) ;
        if (command.equals("discount")) ;
    }

}
