import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

class Discount {
    private int ID;
    private int percentage;
//    private Order order;

    public Discount(int ID, int percentage) {
        this.ID = ID;
        this.percentage = percentage;
    }

//    public Order getOrder() {
//        return order;
//    }

//    public void setOrder(Order order) {
//        this.order = order;
//    }

    public int getID() {
        return ID;
    }

    public int getPercentage() {
        return percentage;
    }
}

enum Status {PENDING, SUBMITTED}

class Order {

    private int ID;
    //    private Customer customer;
    private Status status = Status.PENDING;
    private HashMap<Good, Integer> items = new HashMap<>();
    private Discount discount;

    public Order(int ID) {
        this.ID = ID;
//        this.customer = customer;
    }

    public int getID() {
        return ID;
    }

    public Status getStatus() {
        return status;
    }

    public void submit() {
        this.status = Status.SUBMITTED;
    }

    public void addItem(Good good, int amount) {
        if (status == Status.PENDING)
            items.put(good, items.getOrDefault(good, 0) + amount);
    }

    public void removeItem(Good good) {
        if (status == Status.PENDING)
            items.remove(good);
    }

    public HashMap<Good, Integer> getItems() {
        return items;
    }

    public int calculatePrice() {
        int total = 0;
        for (Good g : items.keySet()) {
            total += g.getPrice() * items.get(g);
        }
        if (discount != null) {
            total *= 1 - discount.getPercentage() / 100.0;
        }
        return total;
    }

    public void addDiscount(Discount discount) {
        if (status == Status.PENDING)
            this.discount = discount;
    }

    // TO DO: not more than one discount for customer

}

class Repository implements Comparable<Repository> {
    private int ID;
    private int capacity;
    private HashMap<Good, Integer> goods = new HashMap<>();

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
        for (Integer i : goods.values()) {
            count += i;
        }
        return capacity - count;
    }

    public HashMap<Good, Integer> getGoods() {
        return goods;
    }

    public void addGood(Good g, int amount) {
        if (amount <= getFreeCapacity()) {
            int newAmount = goods.getOrDefault(g, 0) + amount;
            goods.put(g, newAmount);
        }
    }

    public void removeGood(Good g, int amount) {
        goods.put(g, goods.get(g) - amount);
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
    private ArrayList<Order> orders = new ArrayList<>();

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

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Order[] getTotalOrders() {
        Order[] o = new Order[orders.size()];
        return orders.toArray(o);
    }

    public Order[] getPendingOrders() {
        ArrayList<Order> pendingOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus() == Status.PENDING) {
                pendingOrders.add(o);
            }
        }
        Order[] pendingOrdersArray = new Order[pendingOrders.size()];
        return pendingOrders.toArray(pendingOrdersArray);
    }

    public Order[] getSubmittedOrders() {
        ArrayList<Order> submittedOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus() == Status.SUBMITTED) {
                submittedOrders.add(o);
            }
        }
        Order[] submittedOrdersArray = new Order[submittedOrders.size()];
        return submittedOrders.toArray(submittedOrdersArray);
    }

    public void submitOrder(Order order) {
        balance -= order.calculatePrice();
        order.submit();
    }

    public String report() {
        return ID +
                "," + name +
                "," + balance +
                "," + getTotalOrders().length +
                "," + getSubmittedOrders().length;
    }
}

class Shop {
    private String name;
    private int income;
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Repository> repositories = new ArrayList<>();
    private ArrayList<Discount> discounts = new ArrayList<>();
    private HashMap<Good, Integer> goods = new HashMap<>();

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
        if (!goods.containsKey(g))
            goods.put(g, 0);
    }

    public void incrementGood(Good g, int amount) {
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
        return goods.keySet().toArray(g);
    }

    public void addDiscount(Discount d) {
        discounts.add(d);
    }

    public Order getOrderByID(int ID) {
        for (Customer c : customers) {
            for (Order o : c.getPendingOrders()) {
                if (o.getID() == ID) {
                    return o;
                }
            }
        }
        return null;
    }

    public Customer getCustomerByOrder(Order order) {
        for (Customer c : customers) {
            for (Order o : c.getPendingOrders()) {
                if (o.equals(order)) {
                    return c;
                }
            }
        }
        return null;
    }

    public Discount getDiscountByID(int ID) {
        for (Discount d : discounts) {
            if (d.getID() == ID) {
                return d;
            }
        }
        return null;
    }

    public HashMap<Good, Integer> getItemsSold() {
        return goods;
    }

    private boolean checkOrder(HashMap<Good, Integer> items) {
        for (Good g : items.keySet()) {
            boolean f = false;
            for (Repository r : repositories) {
                if (r.getGoods().containsKey(g) && r.getGoods().get(g) >= items.get(g)) {
                    f = true;
                    break;
                }
            }
            if (!f) return false;
        }
        return true;
    }

    public void submitOrder(Order o) {
        Customer c = getCustomerByOrder(o);
        int price = o.calculatePrice();
        if (c.getBalance() < price) return;
        HashMap<Good, Integer> items = o.getItems();
        if (!checkOrder(items)) return;
        c.submitOrder(o);
        for (Good g : items.keySet()) {
            for (Repository r : repositories) {
                if (r.getGoods().containsKey(g) && r.getGoods().get(g) >= items.get(g)) {
                    r.removeGood(g, items.get(g));
                    break;
                }
            }
        }
        setIncome(getIncome() + price);
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
            Good newGood = new Good(name, ID, price);
            shop.addGood(newGood);
            shop.incrementGood(newGood, amount);
        }
        if (command.equals("repository")) {
            int ID = sc.nextInt();
            int capacity = sc.nextInt();
            shop.addRepository(new Repository(ID, capacity));
        }
        if (command.equals("order")) {
            int ID = sc.nextInt();
            int customerID = sc.nextInt();
            getCustomerById(customerID, shop.getCustomers()).addOrder(new Order(ID));
        }
        if (command.equals("balance")) {
            int ID = sc.nextInt();
            int amount = sc.nextInt();
            Customer c = getCustomerById(ID, shop.getCustomers());
            if (c != null) {
                c.setBalance(c.getBalance() + amount);
            }
        }
        if (command.equals("item")) {
            int orderID = sc.nextInt();
            int goodID = sc.nextInt();
            int amount = sc.nextInt();
            Order o = shop.getOrderByID(orderID);
            if (o != null)
                o.addItem(getGoodById(goodID, shop.getGoods()), amount);
        }
        if (command.equals("discount")) {
            int ID = sc.nextInt();
            int percentage = sc.nextInt();
            shop.addDiscount(new Discount(ID, percentage));
        }
    }

    private static void reportCommand(String command) {
        if (command.equals("customers")) {
            for (Customer c : shop.getCustomers()) {
                System.out.println(c.report());
            }
        }
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
        if (command.equals("item")) {
            int orderID = sc.nextInt();
            int goodID = sc.nextInt();
            shop.getOrderByID(orderID).removeItem(getGoodById(goodID, shop.getGoods()));
        }
    }

    private static void submitCommand(String command) {
        if (command.equals("order")) {
            int orderID = sc.nextInt();
            Order order = shop.getOrderByID(orderID);
            if (order != null)
                shop.submitOrder(order);
        }
        if (command.equals("discount")) {
            int discountID = sc.nextInt();
            int orderID = sc.nextInt();
            shop.getOrderByID(orderID).addDiscount(shop.getDiscountByID(discountID));
        }
    }

    private static Customer getCustomerById(int ID, Customer[] customers) {
        for (Customer c : customers) {
            if (c.getID() == ID) {
                return c;
            }
        }
        return null;
    }

//    private static Order getOrderById(int ID, Order[] orders) {
//        for (Order o : orders) {
//            if (o.getID() == ID) {
//                return o;
//            }
//        }
//        return null;
//    }

    private static Good getGoodById(int ID, Good[] goods) {
        for (Good g : goods) {
            if (g.getID() == ID) {
                return g;
            }
        }
        return null;
    }


}
