package oj.beans

import groovy.transform.ToString

@ToString
class Work extends Entity {
    String project
    String company
    Double hours
    Date date
    String description
    String nickname
    String fromTime
    String toTime
}
