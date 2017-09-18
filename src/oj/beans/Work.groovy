package oj.beans

import groovy.transform.ToString

@ToString
class Work extends Entity {
    String companyId
    String projectId
    Double hours
    Date date
    String description
}
