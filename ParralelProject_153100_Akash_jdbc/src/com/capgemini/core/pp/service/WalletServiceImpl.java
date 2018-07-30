package com.capgemini.core.pp.service;

import java.math.BigDecimal;

import java.util.Scanner;

import com.capgemini.core.pp.beans.Customer;
import com.capgemini.core.pp.beans.Wallet;
import com.capgemini.core.pp.exception.InsufficientBalanceException;
import com.capgemini.core.pp.exception.InvalidInputException;
import com.capgemini.core.pp.repo.WalletRepo;
import com.capgemini.core.pp.repo.WalletRepoImpl;

public class WalletServiceImpl implements WalletService {

	WalletRepo repo = new WalletRepoImpl();
	Customer customer;
	Wallet wallet;

	Scanner str = new Scanner(System.in);

	public WalletServiceImpl(WalletRepo repo) {
		this.repo = repo;
	}

	public WalletServiceImpl() {
	}

	@Override
	public Customer createAccount(String name, String mobileNo, BigDecimal amount) throws InvalidInputException {

		wallet = new Wallet(amount);
		customer = new Customer(name, mobileNo, wallet);
		boolean b1 = isValid(customer);

		if (b1) {
			boolean b = repo.save(customer);
			if (b)
				return customer;
			else
				throw new InvalidInputException("Account not created");

		} else {
			throw new InvalidInputException("Incorrect input");
		}

	}

	@Override
	public Customer showBalance(String mobileno) throws InvalidInputException {
		Customer customer = repo.findOne(mobileno);
		if (customer != null) {
			return customer;
		} else
			throw new InvalidInputException("Invalid mobile no ");
	}

	@Override
	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount)
			throws InvalidInputException, InsufficientBalanceException {

		Customer customerSource = repo.findOne(sourceMobileNo);
		Customer customerTarget = repo.findOne(targetMobileNo);
		if (customerSource != null && customerTarget != null) {
			int i = customerSource.getWallet().getBalance().compareTo(amount);

			if (i != -1) {
				Wallet wallet0 = customerSource.getWallet();
				wallet0.setBalance(wallet0.getBalance().subtract(amount));
				customerSource.setWallet(wallet0);
				Wallet wallet1 = customerTarget.getWallet();
				wallet1.setBalance(wallet1.getBalance().add(amount));
				customerTarget.setWallet(wallet1);
				repo.update(customerSource);
				repo.update(customerTarget);

				return customerSource;
			}

			else {
				throw new InsufficientBalanceException("Balance insufficient");
			}

		} else {
			throw new InvalidInputException("Invalid phone number ");
		}

	}

	@Override
	public Customer depositAmount(String mobileNo, BigDecimal amount) throws InvalidInputException {
		Customer customer = repo.findOne(mobileNo);
		if (customer != null) {
			wallet = customer.getWallet();
			wallet.setBalance(wallet.getBalance().add(amount));

			customer.setWallet(wallet);
			customer = repo.update(customer);
			return customer;
		} else
			throw new InvalidInputException("Invalid mobile no ");

	}

	@Override
	public Customer withdrawAmount(String mobileNo, BigDecimal amount) throws InvalidInputException, InsufficientBalanceException {
		Customer customer = repo.findOne(mobileNo);
		if (customer != null) {

			wallet = customer.getWallet();
			// amount validation here
			int i = wallet.getBalance().compareTo(amount);
			if (i != -1) {
				wallet.setBalance(wallet.getBalance().subtract(amount));

				customer.setWallet(wallet);
				customer = repo.update(customer);
				return customer;
			} else {
				throw new InsufficientBalanceException("INSUFFICIENT BALANCE");
			}

		} else
			throw new InvalidInputException("Invalid mobile no ");

	}

	@Override
	public boolean isValid(Customer customer) throws InvalidInputException {
		boolean b = false;
		if (String.valueOf(customer.getMobileNo()).matches("[1-9][0-9]{9}") && customer.getMobileNo() != null) {
			if (customer.getName().matches("[A-Z][a-z]*") && customer.getName() != null) {
				b=true;
			} else
				throw new InvalidInputException("Invalid name");
		} else
			throw new InvalidInputException("Invalid phone");
		return b;

	}

	public void truncate() {
		boolean b = ((WalletRepoImpl) repo).truncate();
	}

}
