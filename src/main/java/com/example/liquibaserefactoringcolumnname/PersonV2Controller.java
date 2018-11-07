package com.example.liquibaserefactoringcolumnname;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/v2/persons")
public class PersonV2Controller {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Person> getPersons() {
        return jdbcTemplate.query("SELECT * FROM person", new PersonRowMapper());
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM person WHERE id = ?", new PersonRowMapper(), id);
    }

    @PostMapping
    public Person createPerson(@RequestBody CreatePersonCommand command) {
        jdbcTemplate.update("INSERT INTO person (lastname, new_firstname, state) VALUES (?, ?, ?)",
                command.getLastName(), command.getFirstName(), command.getState().name());
        return jdbcTemplate.queryForObject("SELECT id, lastname, new_firstname, state FROM person WHERE id = (SELECT currval('person_id_seq'))", new PersonRowMapper());
    }

    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable("id") int id, @RequestBody UpdatePersonCommand command) {
        jdbcTemplate.update("UPDATE person SET lastname = ?, new_firstname = ?, state = ? WHERE id = ?",
                command.getLastName(), command.getFirstName(), command.getState().name(), id);
        return getPerson(id);
    }

    private static class PersonRowMapper implements RowMapper<Person> {

        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person =new Person();
            person.setId(rs.getInt("id"));
            person.setLastName(rs.getString("lastname"));
            person.setFirstName(rs.getString("new_firstname"));
            person.setState(State.valueOf(rs.getString("state")));
            return person;
        }

    }

}
