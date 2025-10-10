package bci.core;

import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Creator implements Serializable {

    @Serial
    private static final long serialVersionUID = 5840056146680662490L;

    private final String _name;
    private final List<Work> _works;

    public Creator(String name) {
        _name = name;
        _works = new ArrayList<>();
    }

    public String getName() {
        return _name;
    }

    public List<Work> getWorks() {
        List<Work> worksCopy = new ArrayList<>(_works);

        worksCopy.sort(Comparator.comparing(w -> w.getTitle().toLowerCase()));

        return Collections.unmodifiableList(worksCopy);
    }

    public void addWork(Work work) {
        if (work != null && !_works.contains(work)) {
            _works.add(work);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Creator creator && _name.equals(creator._name);
    }
}
