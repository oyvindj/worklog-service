package oj.beans

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString
class Todo extends Entity {
    String projectId
    String companyId
    Date date
    String description
    String nickname
}
