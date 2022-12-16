package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.RegisterUserDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class TransferController {
    private JdbcAccountDao accountDao;
    private TransferDao transferDao;

    public TransferController(JdbcAccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void transfer(@Valid @RequestBody Transfer transfer, Principal principal) {
        if (!transferDao.sendMoney(transfer, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer failed.");
        }
    }

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public List<Transfer> listTransfers(Principal principal) {
        return transferDao.listMyTransfers(principal);
    }

    @RequestMapping(path = "/get", method = RequestMethod.GET)
    public Transfer getById(@Valid @RequestBody int transferId) {
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(path = "/accounts", method = RequestMethod.GET)
    public List<String> listUsers(Principal principal){
        return transferDao.listUsers(principal);
    }

}
