package com.sunhill.repository;

import com.sunhill.entity.Account;
import com.sunhill.exception.NotFoundException;

import java.util.HashMap;

/**
 * Created by javierm on 26/11/18.
 */
public class AccountRepository<A extends Account> {
    //TODO: Change by connection to bbdd
    HashMap<Long, A> accounts = new HashMap<>();
    long index = 0;

    public long insert(A account) {
        accounts.put(++index, account);
        return index;
    }

    public A findById(long id) throws NotFoundException {
        A account = accounts.get(id);
        if (account == null) throw new NotFoundException();
        return account;
    }


}
