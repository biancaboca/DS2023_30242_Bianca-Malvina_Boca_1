package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.UserDTO;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.PersonRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.util.ClassUtils.isPresent;

@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<UserDTO> findPersons() {
        List<User> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDetailsDTO findPersonById(UUID id) {
        Optional<User> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }

    public PersonDetailsDTO updateName(UUID id, String name) {
        Optional<User> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        else
        {
            User user = prosumerOptional.get();
            user.setName(name);
            personRepository.save(user);


        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }
    public PersonDetailsDTO updateRole(UUID id, String role) {
        Optional<User> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        else
        {
            User user = prosumerOptional.get();
            user.setRole(role);
            personRepository.save(user);


        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }
    public PersonDetailsDTO updateNameUsernamePassword(UUID id,String name, String Username, String Password) {
        Optional<User> prosumerOptional = personRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        else
        {
            User user = prosumerOptional.get();
            user.setName(name);
            user.setUsername(Username);
            user.setPassword(Password);
            personRepository.save(user);


        }
        return PersonBuilder.toPersonDetailsDTO(prosumerOptional.get());
    }

    public UUID insert(PersonDetailsDTO personDTO) {
        User person = PersonBuilder.toEntity(personDTO);
        person = personRepository.save(person);
        LOGGER.debug("Person with id {} was inserted in db", person.getId());
        return person.getId();
    }

    public void deletePersonById(UUID id) {
        User person = personRepository.findById(id).get();
        personRepository.delete(person);
        LOGGER.debug("Person with id {} was deleted from db", person.getId());
    }

    public User userSignUp(String name, String username, String password)
    {
        User user = new User();
        user. setPassword(password);
        user.setUsername(username);
        user.setName(name);
        user.setRole("user");
        return user;
    }

    public UUID userLogin(String username, String password) {

        User user = personRepository.findUserByUsernameAndPassword(username, password).get();
        if (user != null) {
            UUID personID = user.getId();
            return personID;
        } else {
            return null;
        }
    }

    public Boolean verifyingUser(UUID id) {
        Optional<User> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            User person = personOptional.get();
            if ("user".equals(person.getRole())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }



}
