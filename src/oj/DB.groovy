package oj

import oj.beans.Company
import oj.beans.Entity
import oj.beans.Project
import oj.beans.Todo
import oj.beans.User
import oj.beans.Work
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
interface TodoDB extends MongoRepository<Todo, String> {
    List<Todo> findByUserId(String userId)
}
@Component
interface WorkDB extends MongoRepository<Work, String> {
    List<Work> findByUserId(String userId)
    List<Work> findByUserIdOrderByFromDateTimeDesc(String userId)
    List<Work> findByUserIdAndDateBetween(String userId, Date start, Date end)
}
@Component
interface CompanyDB extends MongoRepository<Company, String> {
    List<Company> findByUserId(String userId)
}
@Component
interface ProjectDB extends MongoRepository<Project, String> {
    List<Project> findByUserId(String userId)
    List<Project> findByCompanyId(String companyId)
}
@Component
interface UserDB extends MongoRepository<User, String> {
}

interface EntityDB<T extends Entity> extends MongoRepository<? extends Entity, String> {
    List<T> findByUserId(String userId)
}
