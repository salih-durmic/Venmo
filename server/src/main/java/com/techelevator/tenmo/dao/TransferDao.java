package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {
        boolean sendMoney(Transfer transfer);
}
