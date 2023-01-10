package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: Seller findById ===");
        Seller seller = sellerDao.findById(2);
        System.out.println(seller);

        System.out.println("\n === TEST 2: Seller findByDepartment ===");
        Department department = new Department(2, null);
        List<Seller> sellerList = sellerDao.findByDepartment(department);
        for (Seller obj : sellerList) {
            System.out.println(obj);
        }

        System.out.println("\n === TEST 3: Seller findByAll ===");
        List<Seller> findAll = sellerDao.findAll();
        for (Seller obj2 : findAll) {
            System.out.println(obj2);
        }

        System.out.println("\n === TEST 4: Seller Insert ===");
        Seller newSeller = new Seller(null, "Menneck", "Menneck@menneck.com", new Date(), 10000.00, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());

        System.out.println("\n === TEST 5: Seller Update ===");
        seller = sellerDao.findById(1);
        seller.setName("Mbappé");
        sellerDao.update(seller);

        System.out.println("\n === TEST 6: Seller Delete ===");
        System.out.println("Enter id for delete test: ");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        sellerDao.deleteById(id);
        System.out.println("Delete completed");

        sc.close();

    }
}