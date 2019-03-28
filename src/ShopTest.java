public class ShopTest {
    public static void main(String[] args) {
        Shop shop = new Shop("Test");

        shop.addCustomer(new Customer("Radin", 1));
        shop.addRepository(new Repository(1, 4));
        shop.addRepository(new Repository(2, 4));
        shop.addRepository(new Repository(3, 4));
        shop.addGood(new Good("Ice Cream", 1, 50000));
        shop.addGood(new Good("Chips", 2, 50000));
        shop.addGood(new Good("Milk", 3, 20000));
        shop.incrementGood(shop.getGoods()[0], 3);
        System.out.println(shop);
        shop.incrementGood(shop.getGoods()[0], 1);
        System.out.println(shop);
        shop.incrementGood(shop.getGoods()[0], 1);
//        shop.incrementGood(shop.getGoods()[0], 1);
//        shop.incrementGood(shop.getGoods()[0], 1);
        System.out.println(shop);
    }
}
