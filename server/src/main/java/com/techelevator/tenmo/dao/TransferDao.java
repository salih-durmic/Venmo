package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {
        boolean sendMoney(Transfer transfer);
        List<Transfer> listMyTransfers(Principal principal);
        Transfer getTransferById(int transferId);
}
