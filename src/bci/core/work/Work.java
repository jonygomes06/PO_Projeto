package bci.core.work;

import java.io.Serial;
import java.io.Serializable;

public class Work implements Serializable {
    @Serial
    private static final long serialVersionUID = -7478614181433606768L;

    private static int _nextId = 0;

    private int _id;
}
