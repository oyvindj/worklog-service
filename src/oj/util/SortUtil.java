package oj.util;

import oj.beans.Todo;
import oj.beans.Work;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

public class SortUtil {
    public static Comparator<Work> getWorkComparator(String sortKey) {
        String key = sortKey.split("_")[0];
        String order = null;
        if(sortKey.split("_").length > 1) {
            order = sortKey.split("_")[1];
        }
        Comparator<Work> comparator = null;
        switch (key) {
            case "description":
                comparator = (t1, t2) -> t1.getDescription().compareToIgnoreCase(t2.getDescription());
                break;
            case "date":
                comparator = (t1, t2) -> t2.getDate().compareTo(t1.getDate());
                break;
            case "projectId":
                comparator = comparing(Work::getProjectId);
                break;
            case "companyId":
                comparator = comparing(Work::getCompanyId);
                break;
            case "nickname":
                comparator = comparing(Work::getNickname);
                break;
            default:
                comparator = comparing(Work::getId);
                break;
        }
        if((order != null) && isReversed(order)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
    public static Comparator<Todo> getComparator(String sortKey) {
        String key = sortKey.split("_")[0];
        String order = null;
        if(sortKey.split("_").length > 1) {
            order = sortKey.split("_")[1];
        }
        Comparator<Todo> comparator = null;
        switch (key) {
            case "description":
                comparator = (t1, t2) -> t1.getDescription().compareToIgnoreCase(t2.getDescription());
                break;
            case "date":
                comparator = (t1, t2) -> t2.getDate().compareTo(t1.getDate());
                break;
            case "projectId":
                comparator = comparing(Todo::getProjectId);
                break;
            case "companyId":
                comparator = comparing(Todo::getCompanyId);
                break;
            case "nickname":
                comparator = comparing(Todo::getNickname);
                break;
            default:
                comparator = comparing(Todo::getId);
                break;
        }
        if((order != null) && isReversed(order)) {
            comparator = comparator.reversed();
        }
        return comparator;
    }
    private static Boolean isReversed(String order) {
        if(order.equals("desc")) {
            return false;
        } else if(order.equals("asc")) {
            return true;
        } else {
            throw new RuntimeException("unknown order");
        }
    }

    public static void main(String[] args) {
        System.out.println(getComparator("date_asc"));
        Todo t1 = new Todo();
        t1.setDescription("aaa");
        t1.setCompanyId("1");
        Todo t2 = new Todo();
        t2.setDescription("bbb");
        t2.setCompanyId("3");
        Todo t3 = new Todo();
        t3.setDescription("ccc");
        t3.setCompanyId("2");
        List<Todo> list = Arrays.asList(t1, t3, t2);
        Comparator<Todo> c = getComparator("description_desc");
        list.sort(c);
        System.out.println(list);
        c = getComparator("companyId_asc");
        list.sort(c);
        System.out.println(list);


    }
}
