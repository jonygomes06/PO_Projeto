package bci.core.work;

import bci.core.user.Notification;
import bci.core.user.NotificationType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class WorkObserver implements Serializable {
    @Serial
    private static final long serialVersionUID = 7147111111111111124L;

    private transient final List<NotificationType> _subscribedTypes = new ArrayList<>();

    public List<NotificationType> getSubscribedTypes() {
        return _subscribedTypes;
    }

    public void subscribeToNotificationType(NotificationType type) {
        if (!_subscribedTypes.contains(type)) {
            _subscribedTypes.add(type);
        }
    }

    public void unsubscribeFromNotificationType(NotificationType type) {
        _subscribedTypes.remove(type);
    }

    public abstract void update(Notification notification);
}
