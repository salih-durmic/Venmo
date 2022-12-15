package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
        int senderId = transfer.getSenderId();
        int receiverId = transfer.getReceiverId();

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

    public Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setSenderId(rowSet.getInt("sender_id"));
        transfer.setReceiverId(rowSet.getInt("receiver_id"));
        return transfer;
    }



}
