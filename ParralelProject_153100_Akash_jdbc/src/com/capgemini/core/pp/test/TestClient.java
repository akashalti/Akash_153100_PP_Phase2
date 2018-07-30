package com.capgemini.core.pp.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.capgemini.core.pp.beans.Customer;
import com.capgemini.core.pp.beans.Wallet;
import com.capgemini.core.pp.exception.InsufficientBalanceException;
import com.capgemini.core.pp.exception.InvalidInputException;
import com.capgemini.core.pp.repo.WalletRepoImpl;
import com.capgemini.core.pp.service.WalletService;
import com.capgemini.core.pp.service.WalletServiceImpl;

public class TestClient {

	
	
	WalletService service;
	WalletRepoImpl repo;
	Customer customer1,customer2,customer3;
	

	@Before
	public void initialiseData1() throws InvalidInputException {
		
		service=new WalletServiceImpl();
		((WalletServiceImpl) service).truncate();
		customer1 = service.createAccount("Tony", "9876543210", new BigDecimal(9000));
		customer2 = service.createAccount("Peter","9513578426", new BigDecimal(5000));
		customer3 = service.createAccount("Steve","9514786320",new BigDecimal(7500));
}

	@Test
	public void testShowBalance1() throws InvalidInputException
	{
		Customer customer = new Customer();
		customer = service.showBalance("9876543210");
		assertEquals(new BigDecimal(9000), customer.getWallet().getBalance());
	}
	
	@Test
	public void testShowBalance2() throws InvalidInputException
	{
		Customer customer = new Customer();
		customer = service.showBalance("9513578426");
		assertEquals(new BigDecimal(5000), customer.getWallet().getBalance());
	}
	
	@Test
	public void testShowBalance3() throws InvalidInputException
	{
		Customer customer = new Customer();
		customer = service.showBalance("9514786320");
		assertEquals(new BigDecimal(7500), customer.getWallet().getBalance());
	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void testWithdraw1() throws InvalidInputException, InsufficientBalanceException
	{
		service.withdrawAmount("9876543210", new BigDecimal(100000));
		
	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void testWithdraw2() throws InvalidInputException, InsufficientBalanceException
	{
		service.withdrawAmount("9513578426", new BigDecimal(66000));
		
	}
	
	@Test(expected = InsufficientBalanceException.class)
	public void testWithdraw3() throws InvalidInputException, InsufficientBalanceException
	{
		service.withdrawAmount("9514786320", new BigDecimal(9001));
		
	}

	
	@Test
	public void testValidation1() throws InvalidInputException {
		Customer customer = new Customer();
		customer = service.createAccount("Natasha", "1234567890", new BigDecimal(1100));
		assertEquals(10,customer.getMobileNo().length());
	}
	
	@Test
	public void testValidation2() throws InvalidInputException {
		Customer customer = new Customer();
		customer = service.createAccount("Natasha", "7894561230", new BigDecimal(1100));
		assertEquals(10,customer.getMobileNo().length());
	}
	
	@Test
	public void testDepositAmount1() throws InvalidInputException {
		Customer customer = new Customer();
		customer=service.depositAmount("9876543210", new BigDecimal(500));
		assertEquals(new BigDecimal(9500), customer.getWallet().getBalance());
	}
	
	@Test
	public void testDepositAmount2() throws InvalidInputException {
		Customer customer = new Customer();
		customer=service.depositAmount("9513578426", new BigDecimal(500));
		assertEquals(new BigDecimal(5500), customer.getWallet().getBalance());
	}
	
	@Test
	public void testDepositAmount3() throws InvalidInputException {
		Customer customer = new Customer();
		customer=service.depositAmount("9514786320", new BigDecimal(500));
		assertEquals(new BigDecimal(8000), customer.getWallet().getBalance());
	}
	
	@Test
	public void testfundTransfer1() throws InvalidInputException, InsufficientBalanceException {
		Customer customer = new Customer();
		customer = service.fundTransfer("9876543210", "9513578426", new BigDecimal(500));
		assertEquals(new BigDecimal(8500), customer.getWallet().getBalance());
	}
	
	
	@Test
	public void testfundTransfer2() throws InvalidInputException, InsufficientBalanceException {
		Customer customer = new Customer();
		customer = service.fundTransfer("9513578426", "9514786320", new BigDecimal(500));
		assertEquals(new BigDecimal(4500), customer.getWallet().getBalance());
	}
	
	
	@Test
	public void testfundTransfer3() throws InvalidInputException, InsufficientBalanceException {
		Customer customer = new Customer();
		customer = service.fundTransfer("9514786320", "9876543210", new BigDecimal(500));
		assertEquals(new BigDecimal(7000), customer.getWallet().getBalance());
	}



	@Test(expected = InvalidInputException.class )
	public void testInvalidInputException() throws InvalidInputException, InsufficientBalanceException {
		
		service.fundTransfer("9563214780", "5489756165", new BigDecimal(8000));
		
	}
	
	@Test(expected = InsufficientBalanceException.class )
	public void testInsufficientBalanceException() throws InvalidInputException, InsufficientBalanceException {
		
		service.fundTransfer("9514786320", "9513578426", new BigDecimal(800000));
		
	}
	
	@Test(expected = NullPointerException.class)
	public void testfindOneRepo() throws InvalidInputException {
		Customer customer = new Customer("","",new Wallet(null));
		customer = repo.findOne(customer.getMobileNo());
		
	}
	
	@Test
	public void testIsValid1() throws InvalidInputException {
		Customer customer = new Customer("Scarlett","7387678820",new Wallet(new BigDecimal(150000)));
		assertTrue(service.isValid(customer));
	}
	
	@Test
	public void testIsValid2() throws InvalidInputException {
		Customer customer = new Customer("Steve","9563214780",new Wallet(new BigDecimal(5000)));
		assertTrue(service.isValid(customer));
	}
	@Test(expected = NumberFormatException.class)
	public void testException() throws InvalidInputException {
		Customer customer = new Customer("Stephen","9541853333",new Wallet(new BigDecimal("abc")));
		assertTrue(service.isValid(customer));
	}

	
}

























