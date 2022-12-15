package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean sendMoney(Transfer transfer){
        String sql = "BEGIN TRANSACTION; UPDATE account SET balance = balance - ? WHERE account_id = ?; " +
                "UPDATE account SET balance = balance + ? WHERE account_id = ?; COMMIT;";
        BigDecimal amount = transfer.getAmount();
        String sql2 = "SELECT balance FROM account WHERE user_id = ?";

        int senderId = transfer.getSenderId();
        int receiverId = transfer.getReceiverId();
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql2, senderId);
        BigDecimal balance = new BigDecimal(0);
        if (result.next()) {
            balance = result.getBigDecimal("balance");
        }
        if (amount.compareTo(balance) < 0 && amount.equals(new BigDecimal(0))) {
            return false;
        }
        if(senderId == receiverId){
            return false;
        }

        try {
            jdbcTemplate.update(sql, amount, senderId, amount, receiverId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Transfer> listMyTransfers(Transfer transfer) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE sender_id = ? OR receiver_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer.getSenderId(), transfer.getReceiverId());
        while(results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer.getTransferId());
        if (results.next()){
            transfer = mapRowToTransfer(results);
        }
        return transfer;

    }


    public Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setSenderId(rowSet.getInt("sender_id"));
        transfer.setReceiverId(rowSet.getInt("receiver_id"));
        return transfer;
    }


}
