package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class AccountController {
    private JdbcAccountDao accountDao;
    private UserDao userDao;

    public AccountController(JdbcAccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public BigDecimal checkBalance(Principal principal){
        User user = userDao.findByUsername(principal.getName());
        Account account = accountDao.getAccount(user.getUsername());
        return account.getBalance();
    }


}
