package com.sunhill.repository;

import com.sunhill.entity.Account;
import com.sunhill.exception.NotFoundException;

import java.util.HashMap;

/**
 * Created by javierm on 26/11/18.
 */
public class AccountRepository {
    //TODO: Change by connection to bbdd
    HashMap<Long, Account> accounts = new HashMap<>();
    long index = 0;

    public long insert(Account account){
        accounts.put(++index, account);
        return index;
    }

    public Account findById(long id) throws NotFoundException{
         Account account = accounts.get(id);
        if(account==null) throw new NotFoundException();
        return account;
    }


}
