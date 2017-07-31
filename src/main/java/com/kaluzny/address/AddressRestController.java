package com.kaluzny.address;

import com.kaluzny.exception.AlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.List;

@Service
@RestController("address")
@RequestMapping("/api/v1/")
public class AddressRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressRestController.class);

    private AddressService AddressService;

    @Inject
    public AddressRestController(AddressService AuthorService) {
        this.AddressService = AuthorService;
    }

    /* Create a address */
    @RequestMapping(
            value = "address",
            method = RequestMethod.POST)
    public ResponseEntity<Address> createAddress(@RequestBody Address address, UriComponentsBuilder ucBuilder) {
        LOGGER.debug(">>> Creating address with id: " + address.getId());
        if (AddressService.isExist(address)) {
            LOGGER.debug("An address with name " + address.getId() + "exist.");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        AddressService.save(address);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("address/{id}").buildAndExpand(address.getId()).toUri());
        return new ResponseEntity<>(address, headers, HttpStatus.CREATED);
    }

    /* Reading single address */
    @RequestMapping(
            value = "address/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Address> getAddress(@PathVariable("id") int id) {
        LOGGER.debug("Fetching address with id: " + id);
        Address address = AddressService.findById(id);
        if (address == null) {
            LOGGER.debug("Address with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    /*Reads all address*/
    @RequestMapping(
            value = "address",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Address>> listAllAddress() {
        LOGGER.debug("Received request to list all address");
        List<Address> address = AddressService.findAll();
        if (address.isEmpty()) {
            LOGGER.debug("address do not have.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    /* Update a address*/
    @RequestMapping(
            value = "address/{id}",
            method = RequestMethod.PUT)
    public ResponseEntity<Address> updateFromDB(@PathVariable("id") int id,
                                               @RequestBody Address address) {
        LOGGER.debug(">>> Updating address with id: " + id);
        Address currentAddress = AddressService.findById(id);

        if (currentAddress == null) {
            LOGGER.debug("<<< Address with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentAddress.setModified(address.getModified());
        currentAddress.setCountry(address.getCountry());
        currentAddress.setCity(address.getCity());
        currentAddress.setAuthor(address.getAuthor());

        AddressService.update(currentAddress);
        return new ResponseEntity<>(currentAddress, HttpStatus.OK);
    }

    /*Delete a address */
    @RequestMapping(
            value = "address/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Address> deleteAddressFromDB(@PathVariable("id") int id) {
        LOGGER.debug("Fetching & Deleting address with id: " + id + " is successfully removed from database!");

        Address address = AddressService.findById(id);
        if (address == null) {
            LOGGER.debug("Unable to delete. Address with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AddressService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*Delete all address*/
    @RequestMapping(
            value = "address",
            method = RequestMethod.DELETE)
    public ResponseEntity<Address> deleteAllAddress() {
        AddressService.deleteAll();
        LOGGER.debug("Removed all address from database!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleAlreadyExistsException(AlreadyExistsException exception) {
        return exception.getMessage();
    }
}
