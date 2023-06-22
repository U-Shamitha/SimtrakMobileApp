package com.mad_lab.a1_loginpage.inventory;

import com.mad_lab.a1_loginpage.R;

import java.util.ArrayList;
import java.util.List;

public class PriorityData {

    public static List<Priority> getPriorityList(){
        List<Priority> priorityList = new ArrayList<>();

        Priority hint = new Priority();
        hint.setName("Select Priority");
        priorityList.add(hint);

        Priority Urgent = new Priority();
        Urgent.setName("Urgent (within 0-2 days)");
        Urgent.setImage(R.drawable.ic_baseline_warning_24);
        priorityList.add(Urgent);

        Priority MediumUrgent = new Priority();
        MediumUrgent.setName("Medium urgency (within 3-7 days)");
        MediumUrgent.setImage(R.drawable.ic_baseline_warning_24);
        priorityList.add(MediumUrgent);

        Priority NotUrgent = new Priority();
        NotUrgent.setName("Not urgent (over 7 days)");
        NotUrgent.setImage(R.drawable.ic_baseline_warning_24);
        priorityList.add(NotUrgent);

        return priorityList;
    }

}
