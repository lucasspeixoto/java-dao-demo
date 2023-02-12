package org.lspeixoto;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("\n------ TEST 1: seller findById ------");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n------ TEST 2: seller findByDepartment ------");
        Department department = new Department(2, null);
        List<Seller> sellerItemsByDepartment = sellerDao.findByDepartment(department);
        for (Seller _seller : sellerItemsByDepartment) {
            System.out.println(_seller);
        }

        System.out.println("\n------ TEST 3: seller findAll ------");
        List<Seller> allSellerItems = sellerDao.findAll();
        for (Seller _seller : allSellerItems) {
            System.out.println(_seller);
        }
        /**
        System.out.println("\n------ TEST 4: seller insert ------");
        Seller newSeller = new Seller(null, "Liana Fernandes", "lianacgf@hotmail.com", new Date(), 7500.00, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());
        */

        System.out.println("\n------ TEST 5: seller update ------");
        Seller updatedSeller = new Seller(10, "Liana Fernandes Peixoto", "lianacgf@hotmail.com", new Date(), 7500.00, department);
        sellerDao.update(updatedSeller);

        System.out.println("\n------ TEST 6: seller delete ------");
        sellerDao.deleteById(10);
    }
}
