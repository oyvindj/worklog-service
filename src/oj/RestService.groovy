package oj

import groovy.util.logging.Slf4j
import oj.beans.Entity
import oj.beans.Question
import oj.beans.Quiz
import oj.beans.Work
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.websocket.server.PathParam
import java.security.Principal

@RestController
@Slf4j
class RestService {
    @Autowired WorkDB workDB
    @Autowired ProjectDB projectDB
    @Autowired CompanyDB companyDB
    @Autowired UserDB userDB

    public void deleteItem(EntityDB db,  String id, Principal principal) {
        String userId = getUserId(principal)
        Entity item = (Entity) db.findOne(id)
        assert (item.userId == userId)
        db.delete(id)
    }
    Entity getItem(EntityDB db, String id, Principal principal) {
        String userId = getUserId(principal)
        def item = (Entity) db.findOne(id)
        assert (item.userId == userId)
        return item
    }

    public List<? extends Entity> getItemsForUser(EntityDB db, String userId, Principal principal) {
        def item = db.findByUserId(userId)
        return item
    }
    public List<? extends Entity> getItems(EntityDB db, Principal principal) {
        def items = db.findAll()
        log.debug 'returning' + items.size() + ' items'
        return items
    }

    Entity createItem(MongoRepository db, Entity item, Principal principal) {
        item.userId = getUserId(principal)
        return db.save(item)
    }

    @DeleteMapping("/work/{id}")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public void deleteWork(@PathVariable("id") String id, Principal principal) {
        String userId = getUserId(principal)
        def item = (Entity) workDB.findOne(id)
        assert (item.userId == userId)
        workDB.delete(id)
    }

    @PostMapping("/work")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public Work createWork(@RequestBody Work work, Principal principal) {
        String userId = getUserId(principal)
        work.userId = userId
        work.fromDateTime = BL.parseDate(work.date, work.fromTime)
        work.toDateTime = BL.parseDate(work.date, work.toTime)
        return workDB.save(work)
    }

    @GetMapping("/company")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<String> getCompanies(Principal principal) {
        def items = workDB.findAll()
        log.debug "items: " + items
        return Arrays.asList("Acando", "Fitnesspoint", "Oyvind Johansen")
    }
    @GetMapping("/project")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<String> getProjects(Principal principal) {
        def items = workDB.findAll()
        log.debug "items: " + items
        return Arrays.asList("Cristin", "Sprek", "Worklog", "Vlogger")
    }

    @GetMapping("/work")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<Work> getAllWork(Principal principal) {
        // def items = (List<Work>) this.getItems(workDB, principal)
        def items = workDB.findAll()
        log.debug "items: " + items
        return items
    }
    @GetMapping("/work/{id}")
    public Work getWork(@PathVariable("id") String id, Principal principal) {
        return (Work) this.getItem(workDB, id, principal)
    }
    @GetMapping("/work/user/{id}")
    public List<Work> getWorkForUser(@PathVariable("id") String userId, Principal principal) {
        return (List<Work>) this.getItemsForUser(workDB, userId, principal)
    }
    @GetMapping("/users")
    public @ResponseBody String getUsers(Principal principal) {
        log.info "getting users as user: " + getUserId(principal)
        return "{\"users\":[{\"firstname\":\"Richard\", \"lastname\":\"Feynman\"}," +
                "{\"firstname\":\"Marie\",\"lastname\":\"Curie\"}]}";
    }

    static String getUserId(Principal principal) {
        def userId = principal.name.split("\\|")[1]
        return userId
    }
}
