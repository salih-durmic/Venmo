package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.security.Principal;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransferByIdTest(){
        Transfer transfer = new Transfer();
        transfer.setTransferId(3001);
        Transfer newTransfer = sut.getTransferById(3001);
        Assert.assertEquals(transfer.getTransferId(), newTransfer.getTransferId());

    }

//    @Test public void sendMoneyTest(){
//        Transfer transfer = new Transfer();
//        transfer.setSenderId(2001);
//        transfer.setReceiverId(2002);
//        transfer.setAmount(new BigDecimal("10.0"));
//        boolean actual = sut.sendMoney(transfer, "user1");
//
//        Assert.assertTrue(actual);
//
//
//    }



}
