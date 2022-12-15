package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.RegisterUserDTO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class TransferController {
    private JdbcAccountDao accountDao;
    private TransferDao transferDao;

    public TransferController(JdbcAccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public void transfer(@Valid @RequestBody Transfer transfer) {
        if (!transferDao.sendMoney(transfer)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer failed.");
        }
    }





}
