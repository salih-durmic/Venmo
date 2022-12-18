package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {
        boolean sendMoney(Transfer transfer, String username);
        List<Transfer> listMyTransfers(Principal principal);
        Transfer getTransferById(int transferId);
        List<String> listUsers(Principal principal);
}
