package org.lspeixoto;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

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
        for (Seller _seller: sellerItemsByDepartment) {
            System.out.println(_seller);
        }
    }
}
