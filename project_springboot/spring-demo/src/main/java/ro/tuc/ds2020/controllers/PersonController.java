package ro.tuc.ds2020.controllers;


import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.services.PersonService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.util.ClassUtils.isPresent;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {
    private final PersonService personService;

    @Autowired
    private  RestTemplate restTemplate;
    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }


    @GetMapping()
    public ResponseEntity<List<UserDTO>> getPersons() {
        List<UserDTO> dtos = personService.findPersons();
        for (UserDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertProsumer(@Valid @RequestBody PersonDetailsDTO UserDTO) {
        UUID personID = personService.insert(UserDTO);
        ResponseEntity<UUID> responseEntity = restTemplate.postForEntity("http://localhost:8081/personInDevice/personToDevice", personID, UUID.class);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDetailsDTO> getPerson(@PathVariable("id") UUID personId) {
        PersonDetailsDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/{personId}/{name}/{username}/{password}")
    public ResponseEntity<PersonDetailsDTO> updateName(
            @PathVariable("personId") UUID personId,
            @PathVariable("name") String name,
            @PathVariable("username") String username,
            @PathVariable("password") String password)
        {
        //PersonDetailsDTO dto = personService.findPersonById(personId);
        PersonDetailsDTO dto =personService.updateNameUsernamePassword(personId, name,username,password);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") UUID personId) {
            personService.deletePersonById(personId);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{username}/{password}")
    public ResponseEntity<UUID> login(@PathVariable("username") String username, @PathVariable("password") String password)
    {
        UUID prsonID = personService.userLogin(username,password);
        return new ResponseEntity<>(prsonID,HttpStatus.OK);
    }

    @GetMapping(value = "/verifying/{id}")
    public ResponseEntity<Boolean> verifyUser(@PathVariable("id") UUID personId) {
        Boolean isUser = personService.verifyingUser(personId);
        return new ResponseEntity<>(isUser, HttpStatus.OK);
    }



}
