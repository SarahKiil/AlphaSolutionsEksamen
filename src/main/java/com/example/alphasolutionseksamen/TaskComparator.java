package com.example.alphasolutionseksamen;
import com.example.alphasolutionseksamen.model.Task;

import java.util.Comparator;
public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2){
        return t1.getStatus().compareTo(t2.getStatus());
    }
}