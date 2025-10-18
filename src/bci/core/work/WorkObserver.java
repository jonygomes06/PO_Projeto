package bci.core.work;

import bci.core.user.Notification;
import bci.core.user.NotificationType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class WorkObserver implements Serializable {
    @Serial
    private static final long serialVersionUID = 7147111111111111124L;

    private final HashMap<Integer, List<NotificationType>> _subscribedWorks = new HashMap<>();

    public List<NotificationType> getSubscribedTypesOfWork(int workId) {
        return _subscribedWorks.getOrDefault(workId, new ArrayList<>());
    }

    public void subscribeToWorkForNotification(int workId, NotificationType type) {
        _subscribedWorks.putIfAbsent(workId, new ArrayList<>());
        List<NotificationType> types = _subscribedWorks.get(workId);
        if (!types.contains(type)) {
            types.add(type);
        }
    }

    public void unsubscribeFromWorkForNotification(int workId, NotificationType type) {
        if (_subscribedWorks.containsKey(workId)) {
            List<NotificationType> types = _subscribedWorks.get(workId);
            types.remove(type);
            if (types.isEmpty()) {
                _subscribedWorks.remove(workId);
            }
        }
    }

    public abstract void update(Notification notification);
}
