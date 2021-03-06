package lt.asprogramuoju.example.camel.route;

import lt.asprogramuoju.example.camel.domain.Person;
import lt.asprogramuoju.example.camel.service.PersonService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

@Component
public class PersonRoute extends RouteBuilder {

    @Autowired
    private PersonService personService;

    @Override
    public void configure() throws Exception {

        rest("/api").description("Persons REST service")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)

                .get("/person").description("Get all Persons").outType(Person[].class)
                .responseMessage().code(200).message("All Persons successfully returned").endResponseMessage()
                .to("bean:personService?method=getAllPersons")

                .get("/person/{id}").description("Find person by ID")
                .outType(Person.class)
                .param().name("id").type(path).description("The ID of the person").dataType("integer").endParam()
                .responseMessage().code(200).message("Person successfully returned").endResponseMessage()
                .to("bean:personService?method=findPerson(${header.id})")

                .get("/person/actions").description("Persons actions with some insert, update, delete actions")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)
                .outType(Person[].class)
                .responseMessage().code(200).message("All Persons successfully returned").endResponseMessage()
                .route()
                .bean(personService,"makeAdditionalHibernateDefaultAction")
                .endRest()
        ;
    }
}
