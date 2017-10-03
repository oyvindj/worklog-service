package oj

import groovy.util.logging.Slf4j
import oj.beans.Company
import oj.beans.Entity
import oj.beans.Project
import oj.beans.Question
import oj.beans.Quiz
import oj.beans.Todo
import oj.beans.Work
import oj.util.DateUtil
import oj.util.SortUtil
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
import java.util.stream.Collector
import java.util.stream.Collectors

@RestController
@Slf4j
class RestService {
    @Autowired WorkDB workDB
    @Autowired ProjectDB projectDB
    @Autowired CompanyDB companyDB
    @Autowired UserDB userDB
    @Autowired TodoDB todoDB

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

    @DeleteMapping("/company/{id}")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public void deleteCompany(@PathVariable("id") String id, Principal principal) {
        String userId = getUserId(principal)
        def item = (Entity) companyDB.findOne(id)
        assert (item.userId == userId)
        companyDB.delete(id)
    }
    @DeleteMapping("/project/{id}")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public void deleteProject(@PathVariable("id") String id, Principal principal) {
        String userId = getUserId(principal)
        def item = (Entity) projectDB.findOne(id)
        assert (item.userId == userId)
        projectDB.delete(id)
    }

    @GetMapping("/todo")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<Todo> getTodos(
            @RequestParam(defaultValue = "999") Integer limit,
            @RequestParam(defaultValue = "date_desc") String order,
            Principal principal) {
        // Comparator<Todo> byDescription = (t1, t2) -> t1.description.compareTo(t2.description)

        def items = todoDB.findByUserId(getUserId(principal))
        log.debug "items: " + items
        return items.stream().limit(limit).sorted(SortUtil.getComparator(order)).collect(Collectors.toList())
    }


    @DeleteMapping("/todo/{id}")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public void deleteTodo(
            @PathVariable("id") String id,
            Principal principal) {
        String userId = getUserId(principal)
        def item = (Entity) todoDB.findOne(id)
        assert (item.userId == userId)
        todoDB.delete(id)
    }

    @PutMapping("/todo")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public Todo updateTodo(@RequestBody Todo item, Principal principal) {
        assert(item.id != null)
        String userId = getUserId(principal)
        item.userId = userId
        item.date = new Date()
        return todoDB.save(item)
    }
    @PostMapping("/todo")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public Todo createTodo(@RequestBody Todo item, Principal principal) {
        String userId = getUserId(principal)
        item.userId = userId
        item.date = new Date()
        return todoDB.save(item)
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
        work.duration = BL.getDuration(work)
        return workDB.save(work)
    }
    @PostMapping("/company")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public Company createCompany(@RequestBody Company data, Principal principal) {
        String userId = getUserId(principal)
        data.userId = userId
        data.createdDate = new Date()
        return companyDB.save(data)
    }

    @GetMapping("/company")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<Company> getCompanies(Principal principal) {
        def items = companyDB.findAll()
        log.debug "items: " + items
        return items
        // return Arrays.asList("Acando", "Fitnesspoint", "Oyvind Johansen")
    }
    @PostMapping("/project")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public Project createProject(@RequestBody Project data, Principal principal) {
        String userId = getUserId(principal)
        data.userId = userId
        data.createdDate = new Date()
        return projectDB.save(data)
    }
    @GetMapping("/project")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<Project> getAllProjects(Principal principal) {
        def items = projectDB.findAll()
        log.debug "items: " + items
        return items
        // return Arrays.asList("Cristin", "Sprek", "iKnowBase", "Worklog", "Vlogger", "Support")
    }
    @GetMapping("/companyproject")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<Project> getProjects(@RequestParam("companyId") String companyId, Principal principal) {
        def items = projectDB.findByCompanyId(companyId)
        log.debug "items: " + items
        return projectDB.findByCompanyId(companyId)
        // return Arrays.asList("Cristin", "Sprek", "iKnowBase", "Worklog", "Vlogger", "Support")
    }

    @GetMapping("/work/thisweek")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<Work> getWorkThisWeek(Principal principal) {
        def start = DateUtil.getStartOfCurrentWeek()
        def end = DateUtil.getEndOfCurrentWeek()
        def items = workDB.findByUserIdAndDateBetween(getUserId(principal), start, end)
        log.debug "items: " + items
        return items
    }
    @GetMapping("/work")
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    public List<Work> getAllWork(Principal principal) {
        def items = workDB.findByUserIdOrderByFromDateTimeDesc(getUserId(principal))
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
