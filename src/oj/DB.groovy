package oj

import oj.beans.Company
import oj.beans.Entity
import oj.beans.Project
import oj.beans.User
import oj.beans.Work
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

@Component
interface WorkDB extends MongoRepository<Work, String> {
}
@Component
interface CompanyDB extends MongoRepository<Company, String> {
}
@Component
interface ProjectDB extends MongoRepository<Project, String> {
}
@Component
interface UserDB extends MongoRepository<User, String> {
}

interface EntityDB<T extends Entity> extends MongoRepository<? extends Entity, String> {
    List<T> findByUserId(String userId)
}
