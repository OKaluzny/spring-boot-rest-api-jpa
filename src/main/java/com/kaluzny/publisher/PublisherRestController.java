package com.kaluzny.publisher;

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
@RestController("publisher")
@RequestMapping("/api/v1/")
public class PublisherRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherRestController.class);

    private PublisherService publisherService;

    @Inject
    public PublisherRestController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /* Create a publisher */
    @RequestMapping(
            value = "publishers",
            method = RequestMethod.POST)
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher, UriComponentsBuilder ucBuilder) {
        LOGGER.debug(">>> Creating publisher with id: " + publisher.getId());
        if (publisherService.isExist(publisher)) {
            LOGGER.debug("A publisher with name " + publisher.getId() + "exist.");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        publisherService.save(publisher);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("publishers/{id}").buildAndExpand(publisher.getId()).toUri());
        return new ResponseEntity<>(publisher, headers, HttpStatus.CREATED);
    }

    /* Reading single publisher */
    @RequestMapping(
            value = "publishers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Publisher> getPublisher(@PathVariable("id") int id) {
        LOGGER.debug("Fetching publisher with id: " + id);
        Publisher publisher = publisherService.findById(id);
        if (publisher == null) {
            LOGGER.debug("Publisher with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(publisher, HttpStatus.OK);
    }

    /*Reads all publishers*/
    @RequestMapping(
            value = "publishers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Publisher>> listAllPublishers() {
        LOGGER.debug("Received request to list all publishers");
        List<Publisher> publishers = publisherService.findAll();
        if (publishers.isEmpty()) {
            LOGGER.debug("Publishers do not have.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(publishers, HttpStatus.OK);
    }

    /*Update a publisher*/
    @RequestMapping(
            value = "publishers/{id}",
            method = RequestMethod.PUT)
    public ResponseEntity<Publisher> updatePublisherFromDB(@PathVariable("id") int id,
                                                           @RequestBody Publisher publisher) {
        LOGGER.debug(">>> Updating publisher with id: " + id);
        Publisher currentPublisher = publisherService.findById(id);

        if (currentPublisher == null) {
            LOGGER.debug("<<< Publisher with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentPublisher.setModified(publisher.getModified());
        currentPublisher.setName(publisher.getName());


        publisherService.update(currentPublisher);
        return new ResponseEntity<>(currentPublisher, HttpStatus.OK);
    }

    /*Delete a publisher */
    @RequestMapping(
            value = "publishers/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Publisher> deletePublisherFromDB(@PathVariable("id") int id) {
        LOGGER.debug("Fetching & Deleting Publisher with id: " + id + " is successfully removed from database!");

        Publisher publisher = publisherService.findById(id);
        if (publisher == null) {
            LOGGER.debug("Unable to delete. Publisher with id: " + id + ", not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        publisherService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*Delete all publishers*/
    @RequestMapping(
            value = "publishers",
            method = RequestMethod.DELETE)
    public ResponseEntity<Publisher> deleteAllPublishers() {
        publisherService.deleteAll();
        LOGGER.debug("Removed all publishers from database!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUserAlreadyExistsException(AlreadyExistsException exception) {
        return exception.getMessage();
    }
}
