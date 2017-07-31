package com.kaluzny.address;

import java.util.List;

public interface AddressService {

    boolean isExist(Address address);

    Address save(Address address);

    Address findById(int id);

    List<Address> findAll();

    Address update(Address address);

    void delete(int id);

    void deleteAll();
}
