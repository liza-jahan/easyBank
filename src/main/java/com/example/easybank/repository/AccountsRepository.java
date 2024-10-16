package com.example.easybank.repository;

import com.example.easybank.model.Accounts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends CrudRepository<Accounts,Long> {

    Accounts findByCustomerId(long customerId);
}
