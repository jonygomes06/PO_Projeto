package bci.core;

import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Creator implements Serializable {

    @Serial
    private static final long serialVersionUID = 5840056146680662490L;

    private final String _name;
    private List<Work> _works;

    public Creator(String name) {
        _name = name;
        _works = new ArrayList<>();
    }
}
