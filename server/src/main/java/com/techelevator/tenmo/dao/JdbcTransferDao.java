package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
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
            transfer = createNewTransferInDatabase(transfer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Transfer> listMyTransfers(Principal principal) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, sender_id, receiver_id, amount FROM transfer AS t JOIN tenmo_user AS u ON t.sender_id = u.user_id WHERE u.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, principal.getName());
        while(results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        sql = "SELECT transfer_id, sender_id, receiver_id, amount FROM transfer AS t JOIN tenmo_user AS u ON t.receiver_id = u.user_id WHERE u.username = ?;";
        results = jdbcTemplate.queryForRowSet(sql, principal.getName());
        while(results.next()) {
            transfers.add(mapRowToTransfer(results));
        }

        return transfers;
    }

    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()){
            transfer = mapRowToTransfer(results);
        }
        transfer.setTransferId(transferId);
        return transfer;

    }

    public Transfer createNewTransferInDatabase(Transfer transfer){
        int senderUserId = getSenderUserId(transfer);
        int receiverUserId = getReceiverUserId(transfer);
        String sql = "INSERT INTO transfer (sender_id, receiver_id, amount) VALUES (?, ?, ?)  RETURNING transfer_id;";
        int newId = jdbcTemplate.queryForObject(sql, Integer.class, senderUserId, receiverUserId, transfer.getAmount());
        transfer.setTransferId(newId);
        return transfer;
    }

    private int getSenderUserId(Transfer transfer) {
        int senderUserId = 0;
        String sql = "SELECT a.user_id FROM account AS a JOIN tenmo_user AS t ON a.user_id = t.user_id WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transfer.getSenderId());
        while(result.next()) {
            senderUserId = result.getInt("user_id");
        }
        return senderUserId;
    }

    private int getReceiverUserId(Transfer transfer) {
        int receiverUserId = 0;
        String sql = "SELECT a.user_id FROM account AS a JOIN tenmo_user AS t ON a.user_id = t.user_id WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transfer.getReceiverId());
        while (result.next()) {
            receiverUserId = result.getInt("user_id");
        }
        return receiverUserId;
    }

    public Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setSenderId(rowSet.getInt("sender_id"));
        transfer.setReceiverId(rowSet.getInt("receiver_id"));
        return transfer;
    }


}
