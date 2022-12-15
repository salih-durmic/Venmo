package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
        boolean sendMoney(Transfer transfer);
        List<Transfer> listMyTransfers(Transfer transfer);
        Transfer getTransferById(int transferId);
}
